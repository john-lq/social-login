package com.lqjohn.socialmedia.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.lqjohn.socialmedia.R
import com.lqjohn.socialmedia.model.AppSharedPref
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.model.UserAccount
import com.lqjohn.socialmedia.view.SocialMediaActivity
import net.openid.appauth.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

internal open class LoginUnsplashViewModel : LoginViewModel() {
    private lateinit var mAuthorizationService: AuthorizationService
    private lateinit var mAppSharedPref: AppSharedPref
    private var mAuthState: AuthState? = null

    fun onCreate(activity: Activity) {
        mAuthorizationService = AuthorizationService(activity)
        mAppSharedPref = AppSharedPref.getInstance(activity)
        mAuthState = mAppSharedPref.getUnsplashAuthState()
    }

    fun onDestroy() {
        mAuthorizationService.dispose()
    }

    override fun signIn(activity: Activity) {
        oauth2Authorization(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OAUTH2_UNSPLASH_SIGN_IN) {
            handleAuthorizationResponse(resultCode, data)
        }
    }

    override fun signOut() {
        mAuthState = null
        mAppSharedPref.setUnsplashAuthState(null)
    }

    fun getExistToken(): String? = mAuthState?.accessToken

    private fun oauth2Authorization(activity: Activity) {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse(activity.getString(R.string.unsplash_oauth2_authorization_end_point)),
            Uri.parse(activity.getString(R.string.unsplash_oauth2_token_end_point)),
            null
        )
        val clientId = activity.getString(R.string.unsplash_oauth2_client_id)
        val redirectUri = Uri.parse(activity.getString(R.string.unsplash_oauth2_redirect_uri))
        val builder = AuthorizationRequest.Builder(
            serviceConfiguration,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        ).setScopes("public", "read_photos", "read_collections")

        activity.startActivityForResult(
            mAuthorizationService.getAuthorizationRequestIntent(builder.build()),
            OAUTH2_UNSPLASH_SIGN_IN
        )
    }

    private fun handleAuthorizationResponse(resultCode: Int, intent: Intent?) {
        val map = mutableMapOf(
            Pair("client_secret", mAppSharedPref.context.resources.getString(R.string.unsplash_oauth2_secret_id))
        )

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
                        response.createTokenExchangeRequest(map),
                        authState
                    ) { isSuccess, auState, ex ->
                        if (isSuccess && auState != null) {
                            UserProfileRequestTask {
                                userAccount.value = createUserAccount(authState, it)
                            }.execute(
                                UserProfileRequest(
                                    mAppSharedPref.context.resources.getString(R.string.unsplash_api_end_point),
                                    auState.accessToken ?: ""
                                )
                            )
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
                Log.d(
                    SocialMediaActivity.TAG,
                    String.format(
                        "Token Response [ Access Token: %s, ID Token: %s ]",
                        tokenResponse.accessToken,
                        tokenResponse.idToken
                    )
                )
                authState.update(tokenResponse, exception)

                mAuthState = authState
                mAppSharedPref.setUnsplashAuthState(authState)

                authState
            } else {
                Log.d(SocialMediaActivity.TAG, "Token Exchange failed: $exception")
                null
            }

            requestTokenCallback(result != null, result, exception)
        }
    }

    private fun createUserAccount(authState: AuthState, userProfileModel: UserProfileModel?): UserAccount {
        val userAccount = UserAccount().apply {
            token = authState.lastTokenResponse?.accessToken
            loginType = LoginType.UNSPLASH
        }

        userAccount.id = userProfileModel?.username ?: ""
        userAccount.name = userProfileModel?.name
        userAccount.avatarUrl = userProfileModel?.profileImage?.avatarUrl

        return userAccount
    }

    companion object {
        private const val OAUTH2_UNSPLASH_SIGN_IN: Int = 1002
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Get user profile
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class UserProfileRequest(val url: String, val token: String)

    private class UserProfileRequestTask(private val mCallbacks: (jsonStr: UserProfileModel?) -> Unit) :
        AsyncTask<UserProfileRequest, Void, UserProfileModel?>() {
        override fun doInBackground(vararg params: UserProfileRequest?): UserProfileModel? {
            try {
                val request = params[0]
                val service = Retrofit.Builder()
                    .baseUrl(request?.url ?: "https://api.unsplash.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UnsplashService::class.java)
                val response = service.getUserProfile("Bearer ${request?.token}").execute()
                val json = response.body()
                Log.d(SocialMediaActivity.TAG, "User profile: $json")
                return json
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(profileModel: UserProfileModel?) {
            super.onPostExecute(profileModel)
            mCallbacks(profileModel)
        }
    }

    private interface UnsplashService {
        @GET("me")
        fun getUserProfile(@Header("Authorization") authorization: String): Call<UserProfileModel?>
    }

    private class UserProfileModel {
        @SerializedName("username")
        var username: String = ""

        @SerializedName("name")
        var name: String = ""

        @SerializedName("profile_image")
        var profileImage: Avatar? = null

        class Avatar {
            @SerializedName("medium")
            var avatarUrl: String = ""
        }

        override fun toString(): String {
            return "Username: $username, Name: $name, Avatar: ${profileImage?.avatarUrl}"
        }
    }
}