package com.lqjohn.socialmedia.login.unsplash

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.lqjohn.socialmedia.SocialMediaLoginManager
import com.lqjohn.socialmedia.login.LoginSocialMedia
import com.lqjohn.socialmedia.model.UserAccount
import com.lqjohn.socialmedia.view.LoginUnsplashActivity

internal class LoginUnsplash(
    activity: FragmentActivity,
    isCancel: Observer<Boolean>,
    ex: Observer<Exception>,
    userAccount: Observer<UserAccount>
) : LoginSocialMedia(activity, isCancel, ex, userAccount) {
    override fun login() {
        setupViewModel(activity, isCancel, ex, userAccount)
        activity.startActivityForResult(
            Intent(activity, LoginUnsplashActivity::class.java),
            SocialMediaLoginManager.LOGIN_REQUEST_CODE
        )
    }
}