package com.lqjohn.socialmedia.login.unsplash

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.login.LogoutSocialMedia
import com.lqjohn.socialmedia.viewmodel.LoginUnsplashViewModel

internal class LogoutUnsplash(activity: FragmentActivity) : LogoutSocialMedia(activity) {
    override fun logout() {
        try {
            ViewModelProviders.of(activity).get(LoginUnsplashViewModel::class.java).let {
                it.onCreate(activity)
                it.signOut()
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}