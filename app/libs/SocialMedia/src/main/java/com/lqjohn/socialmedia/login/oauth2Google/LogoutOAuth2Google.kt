package com.lqjohn.socialmedia.login.oauth2Google

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.login.LogoutSocialMedia
import com.lqjohn.socialmedia.viewmodel.LoginOAuth2GoogleViewModel

internal class LogoutOAuth2Google(activity: FragmentActivity) : LogoutSocialMedia(activity) {
    override fun logout() {
        try {
            ViewModelProviders.of(activity).get(LoginOAuth2GoogleViewModel::class.java).signOut()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}