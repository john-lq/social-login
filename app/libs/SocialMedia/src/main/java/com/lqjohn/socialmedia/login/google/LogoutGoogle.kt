package com.lqjohn.socialmedia.login.google

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.login.LogoutSocialMedia
import com.lqjohn.socialmedia.viewmodel.LoginFacebookViewModel
import com.lqjohn.socialmedia.viewmodel.LoginGoogleViewModel

internal class LogoutGoogle(activity: FragmentActivity) : LogoutSocialMedia(activity) {
    override fun logout() {
        try {
            ViewModelProviders.of(activity).get(LoginGoogleViewModel::class.java).signOut()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}