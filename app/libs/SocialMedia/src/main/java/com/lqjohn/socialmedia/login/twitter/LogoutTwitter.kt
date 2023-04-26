package com.lqjohn.socialmedia.login.twitter

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.login.LogoutSocialMedia
import com.lqjohn.socialmedia.viewmodel.LoginTwitterViewModel
import com.twitter.sdk.android.core.Twitter

internal class LogoutTwitter(activity: FragmentActivity) : LogoutSocialMedia(activity) {
    override fun logout() {
        try {
            Twitter.initialize(activity)
            ViewModelProviders.of(activity).get(LoginTwitterViewModel::class.java).signOut()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}