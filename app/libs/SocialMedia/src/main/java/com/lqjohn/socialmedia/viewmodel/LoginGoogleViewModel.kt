package com.lqjohn.socialmedia.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.lqjohn.socialmedia.R
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.model.UserAccount
import com.lqjohn.socialmedia.view.SocialMediaActivity

internal open class LoginGoogleViewModel : LoginViewModel() {

    companion object {
        private const val GOOGLE_SIGN_IN: Int = 1000
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    fun onCreate(activity: Activity) {
        val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(activity.getString(R.string.google_server_client_id))
                .requestProfile()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    override fun signIn(activity: Activity) {
//        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(activity)
//        if (account != null) {
//            sendResponse(account)
//        } else {
//            val signInIntent = mGoogleSignInClient.signInIntent
//            activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
//        }

        val signInIntent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    override fun signOut() {
        mGoogleSignInClient.signOut()
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Log.d(SocialMediaActivity.TAG, "Login Google success")
                sendResponse(account)
            } else {
                Log.e(SocialMediaActivity.TAG, "Data is null")
                exception.value = null
            }
        } catch (e: ApiException) {
            Log.d(SocialMediaActivity.TAG, "Login Google error")
            // I have to create a new Exception object because the ApiException class contains
            // a field that is not a java.io.Serializable
            exception.value = Exception(e.message, e.cause)
        }
    }

    private fun sendResponse(account: GoogleSignInAccount) {
        val userAccount = UserAccount()
        userAccount.id = account.id
        userAccount.name = account.displayName
        userAccount.email = account.email
        userAccount.token = account.idToken
        userAccount.gender = null
        userAccount.loginType = LoginType.GOOGLE

        this.userAccount.value = userAccount
    }
}