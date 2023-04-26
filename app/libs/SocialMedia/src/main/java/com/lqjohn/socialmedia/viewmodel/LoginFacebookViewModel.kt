package com.lqjohn.socialmedia.viewmodel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.model.UserAccount
import com.lqjohn.socialmedia.view.SocialMediaActivity
import org.json.JSONObject

internal class LoginFacebookViewModel : LoginViewModel() {
    private var mCallbackManager: CallbackManager = CallbackManager.Factory.create()

    companion object {
        /**
         * See here for more information
         * https://developers.facebook.com/docs/facebook-login/permissions/v3.0#permissions
         */
        private val PERMISSIONS = listOf("public_profile", "user_photos")
        private const val ID = "id"
        private const val NAME = "name"
        //        private const val EMAIL = "email"
//        private const val GENDER = "gender"
        private const val AVATAR_URL = "picture{url}"
        private const val FIELDS = "$ID,$NAME,$AVATAR_URL"
    }

    init {
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d(SocialMediaActivity.TAG, "Login Facebook success")
                if (result != null) {
                    getProfile(result.accessToken)
                }
            }

            override fun onCancel() {
                Log.d(SocialMediaActivity.TAG, "Login Facebook cancel")
                isCancel.value = true
            }

            override fun onError(error: FacebookException?) {
                Log.d(SocialMediaActivity.TAG, "Login Facebook error")
                exception.value = error
            }
        })
    }

    override fun signIn(activity: Activity) {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn) {
            getProfile(accessToken)
        } else {
            LoginManager
                    .getInstance()
                    .setLoginBehavior(LoginBehavior.WEB_ONLY)
                    .logInWithReadPermissions(activity, PERMISSIONS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun signOut() {
        LoginManager.getInstance().logOut()
    }

    /**
     * Get profile of the user by token
     */
    private fun getProfile(accessToken: AccessToken) {
        val graphRequest = GraphRequest.newMeRequest(
                accessToken
        ) { _, response ->
            // The error from the server, or null if there was no server error
            if (response.error == null) {
                sendResponse(response.rawResponse, accessToken.token)
            } else {
                exception.value = response.error.exception
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", FIELDS)
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }

    private fun sendResponse(response: String, token: String) {
        val jsonObject = JSONObject(response)

        val userAccount = UserAccount()
        userAccount.id = getValueFromJson(jsonObject, ID)
        userAccount.name = getValueFromJson(jsonObject, NAME)
        userAccount.avatarUrl = getAvatarUrlFromJson(jsonObject)
        userAccount.token = token
        userAccount.loginType = LoginType.FACEBOOK

        this.userAccount.value = userAccount
    }

    private fun getValueFromJson(jsonObject: JSONObject, key: String): String? {
        return if (jsonObject.has(key)) jsonObject.getString(key) else null
    }

    /**
     * "picture": {
     *    "data": {
     *        "url": "https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=2184842988245111&height=50&width=50&ext=1553931696&hash=AeQjgUtlqfTfP2GA"
     *    }
     * }
     */
    private fun getAvatarUrlFromJson(jsonObject: JSONObject): String? {
        return try {
            jsonObject.getJSONObject("picture")?.getJSONObject("data")?.getString("url")
        } catch (ex: Exception) {
            null
        }
    }
}