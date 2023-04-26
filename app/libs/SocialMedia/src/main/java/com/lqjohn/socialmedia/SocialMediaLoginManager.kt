package com.lqjohn.socialmedia

import android.content.Intent
import com.lqjohn.socialmedia.login.IGetExistingToken
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.IRefreshToken

/**
 * How to use it?
 *
 * Using this class in the {@link FragmentActivity}. I public some methods which
 * help us to login on Facebook or Google
 * {@link #signInGoogle}
 * {@link #signInFacebook()}
 *
 * You have to override the {@link FragmentActivity#onActivityResult}
 * method and involve {@link #onActivityResult} method
 */
class SocialMediaLoginManager {
    // Create a singleton
    // https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/persistence/UsersDatabase.kt
    companion object {
        const val IS_SUCCESS = "com.lqjohn.socialmedia.IS_SUCCESS"
        const val EXCEPTION = "com.lqjohn.socialmedia.EXCEPTION"
        const val DATA = "com.lqjohn.socialmedia.DATA"
        const val LOGIN_REQUEST_CODE = 2000

        private var INSTANCE: SocialMediaLoginManager? = null

        fun getInstance(): SocialMediaLoginManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SocialMediaLoginManager().also { INSTANCE = it }
            }
    }

    private var mILogin: ILogin? = null

    fun login(param: ILogin) {
        mILogin = param
        param.login()
    }

    fun logout(param: ILogout) {
        param.logout()
    }

    fun refreshToken(param: IRefreshToken) {
        param.refreshToken()
    }

    fun getExistingToken(param: IGetExistingToken) {
        param.getExistingToken()
    }

    /**
     * Get data from the {@link Activity#onActivityResult(int, int, Intent) method
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        mILogin?.onActivityResult(requestCode, resultCode, intent)
    }
}