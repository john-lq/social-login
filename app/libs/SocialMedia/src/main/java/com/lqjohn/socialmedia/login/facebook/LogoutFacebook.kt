package com.lqjohn.socialmedia.login.facebook

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.login.LogoutSocialMedia
import com.lqjohn.socialmedia.viewmodel.LoginFacebookViewModel

internal class LogoutFacebook(activity: FragmentActivity) : LogoutSocialMedia(activity) {
    override fun logout() {
        try {
            ViewModelProviders.of(activity).get(LoginFacebookViewModel::class.java).signOut()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}