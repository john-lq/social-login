package com.lqjohn.socialmedia.login.oauth2Google

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.login.IRefreshToken
import com.lqjohn.socialmedia.viewmodel.LoginOAuth2GoogleViewModel

internal class RefreshTokenOAuth2Google(
    private val activity: FragmentActivity,
    private val callback: (newAccessToken: String?) -> Unit
) : IRefreshToken {
    override fun refreshToken() {
        try {
            ViewModelProviders.of(activity).get(LoginOAuth2GoogleViewModel::class.java).let {
                it.onCreate(activity)
                it.refreshToken(callback)
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}