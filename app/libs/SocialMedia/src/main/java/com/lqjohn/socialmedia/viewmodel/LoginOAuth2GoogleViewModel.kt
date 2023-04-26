package com.lqjohn.socialmedia.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lqjohn.socialmedia.R
import com.lqjohn.socialmedia.model.AppSharedPref
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.model.UserAccount
import com.lqjohn.socialmedia.view.SocialMediaActivity
import net.openid.appauth.*
import java.nio.charset.Charset

internal open class LoginOAuth2GoogleViewModel : LoginViewModel() {
    private lateinit var mAuthorizationService: AuthorizationService
    private lateinit var mAppSharedPref: AppSharedPref
    private var mAuthState: AuthState? = null

    fun onCreate(activity: Activity) {
        mAuthorizationService = AuthorizationService(activity)
        mAppSharedPref = AppSharedPref.getInstance(activity)
        mAuthState = mAppSharedPref.getAuthState()
    }

    fun onDestroy() {
        mAuthorizationService.dispose()
    }

    override fun signIn(activity: Activity) {
        oauth2Authorization(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OAUTH2_GOOGLE_SIGN_IN) {
            handleAuthorizationResponse(resultCode, data)
        }
    }

    override fun signOut() {
        mAuthState = null
        mAppSharedPref.setAuthState(null)
    }

    fun needsTokenRefresh(): Boolean = mAuthState?.needsTokenRefresh ?: false

    fun refreshToken(requestTokenCallback: (newAccessToken: String?) -> Unit) {
        val authState = mAuthState
        if (authState != null) {
            performRefreshTokenRequest(authState) { _, accessToken, _ ->
                requestTokenCallback(accessToken)
            }
        } else {
            requestTokenCallback(null)
        }
    }

    private fun oauth2Authorization(activity: Activity) {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse(activity.getString(R.string.google_oauth2_authorization_end_point)),
            Uri.parse(activity.getString(R.string.google_oauth2_token_end_point)),
            null
        )
        val clientId = activity.getString(R.string.google_oauth2_client_id)
        val redirectUri = Uri.parse(activity.getString(R.string.google_oauth2_redirect_uri))
        val builder = AuthorizationRequest.Builder(
            serviceConfiguration,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        ).setScopes("profile", "https://www.googleapis.com/auth/photoslibrary.readonly")

        activity.startActivityForResult(
            mAuthorizationService.getAuthorizationRequestIntent(builder.build()),
            OAUTH2_GOOGLE_SIGN_IN
        )
    }

    private fun handleAuthorizationResponse(resultCode: Int, intent: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (intent == null) {
                    exception.value = Exception("Intent is null")
                    return
                }

                val response = AuthorizationResponse.fromIntent(intent)
                val error = AuthorizationException.fromIntent(intent)
                val authState = AuthState(response, error)

                when {
                    error != null -> exception.value = error

                    response != null -> performTokenRequest(
                        response.createTokenExchangeRequest(),
                        authState
                    ) { isSuccess, auState, ex ->
                        if (isSuccess && auState != null) {
                            sendResponse(auState)
                        } else {
                            exception.value = ex
                        }
                    }
                }
            }
            Activity.RESULT_CANCELED -> {
                isCancel.value = true
            }
        }
    }

    @Synchronized
    private fun performTokenRequest(
        tokenRequest: TokenRequest,
        authState: AuthState,
        requestTokenCallback: (isSuccess: Boolean, authState: AuthState?, ex: Exception?) -> Unit
    ) {
        if (!authState.needsTokenRefresh) {
            requestTokenCallback(true, authState, null)
            return
        }

        mAuthorizationService.performTokenRequest(tokenRequest) { tokenResponse, exception ->
            val result = if (exception == null && tokenResponse != null) {
//                Log.d(
//                    SocialMediaActivity.TAG,
//                    String.format(
//                        "Token Response [ Access Token: %s, ID Token: %s ]",
//                        tokenResponse.accessToken,
//                        tokenResponse.idToken
//                    )
//                )
                authState.update(tokenResponse, exception)

                mAuthState = authState
                mAppSharedPref.setAuthState(authState)

                authState
            } else {
                Log.d(SocialMediaActivity.TAG, "Token Exchange failed: $exception")
                null
            }

            requestTokenCallback(result != null, result, exception)
        }
    }

    @Synchronized
    private fun performRefreshTokenRequest(
        authState: AuthState,
        requestTokenCallback: (isSuccess: Boolean, accessToken: String?, ex: Exception?) -> Unit
    ) {
        if (!authState.needsTokenRefresh) {
            requestTokenCallback(true, authState.accessToken, null)
            return
        }

        authState.performActionWithFreshTokens(mAuthorizationService) { accessToken, _, ex ->
            mAuthState = authState
            mAppSharedPref.setAuthState(authState)
            requestTokenCallback(!TextUtils.isEmpty(accessToken), accessToken, ex)
        }
    }

    /**
     * {
     *  "iss": "https://accounts.google.com",
     *  "azp": "",
     *  "aud": "",
     *  "sub": "id_xxxxx",
     *  "at_hash": "",
     *  "name": "Gioan Trang",
     *  "picture": "avatar url",
     *  "given_name": "Gioan",
     *  "family_name": "Trang",
     *  "locale": "en",
     *  "iat": 1554262990,
     *  "exp": 1554266590
     *  }
     */
    private fun sendResponse(authState: AuthState) {
        this.userAccount.value = createUserAccount(authState)
    }

    private fun createUserAccount(authState: AuthState): UserAccount {
        val userAccount = UserAccount().apply {
            token = authState.lastTokenResponse?.accessToken
            loginType = LoginType.GOOGLE
        }

        // idToken is a JSON Web Token (JWT)
        authState.lastTokenResponse?.idToken?.let {
            try {
                val parts = splitToken(it)
                val bytes = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
                val payload = String(bytes, Charset.forName("UTF-8"))
                if (!TextUtils.isEmpty(payload)) {
                    val jsonObject = Gson().fromJson(payload, JsonObject::class.java)
                    userAccount.id = jsonObject.get("sub").asString
                    userAccount.name = jsonObject.get("name").asString
                    userAccount.avatarUrl = jsonObject.get("picture").asString
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        return userAccount
    }

    @Throws(IllegalArgumentException::class)
    private fun splitToken(token: String): Array<String> {
        var parts = token.split(".").toTypedArray()
        if (parts.size == 2 && token.endsWith(".")) {
            // Tokens with alg='none' have empty String as Signature.
            parts = arrayOf(parts[0], parts[1], "")
        }
        if (parts.size != 3) {
            throw IllegalArgumentException(
                String.format(
                    "The token was expected to have 3 parts, but got %s.",
                    parts.size
                )
            )
        }
        return parts
    }

    companion object {
        private const val OAUTH2_GOOGLE_SIGN_IN: Int = 1001
    }
}