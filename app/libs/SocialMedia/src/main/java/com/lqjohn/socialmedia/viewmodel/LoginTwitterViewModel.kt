package com.lqjohn.socialmedia.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.model.UserAccount
import com.lqjohn.socialmedia.view.SocialMediaActivity
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient

internal class LoginTwitterViewModel : LoginViewModel() {
    private val mTwitterAuthClient = TwitterAuthClient()

    /**
     * https://github.com/twitter/twitter-kit-android/wiki/Log-In-with-Twitter
     */
    override fun signIn(activity: Activity) {
        // Check whether the user logged in
        val twitterSession = TwitterCore.getInstance().sessionManager?.activeSession
        if (twitterSession != null) {
            requestEmail(twitterSession)
            return
        }

        // The user doesn't log in yet
        mTwitterAuthClient.authorize(activity, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                if (result != null) {
                    requestEmail(result.data)
                } else {
                    Log.e(SocialMediaActivity.TAG, "Data is null")
                    exception.value = null
                }
            }

            override fun failure(e: TwitterException?) {
                Log.d(SocialMediaActivity.TAG, "Login Twitter error")
                exception.value = e
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data)
    }

    override fun signOut() {
        TwitterCore.getInstance().sessionManager.clearActiveSession()
    }

    private fun requestEmail(twitterSession: TwitterSession) {
        mTwitterAuthClient.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>?) {
                if (result != null)
                    sendResponse(twitterSession, result.data)
                else
                    sendResponse(twitterSession)
            }

            override fun failure(exception: TwitterException?) {
                sendResponse(twitterSession)
            }
        })
    }

    private fun sendResponse(twitterSession: TwitterSession, email: String? = null) {
        val userAccount = UserAccount()
        userAccount.id = twitterSession.userId.toString()
        userAccount.name = twitterSession.userName
        userAccount.email = email
        userAccount.token = twitterSession.authToken.token
        userAccount.gender = null
        userAccount.loginType = LoginType.TWITTER

        this.userAccount.value = userAccount
    }
}