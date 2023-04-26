package com.lqjohn.socialmedia.builder.unsplash

import com.lqjohn.socialmedia.builder.LoginBuilder
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.unsplash.LoginUnsplash

class LoginUnsplashBuilder : LoginBuilder() {
    override fun build(): ILogin {
        validate()
        return LoginUnsplash(activity, isCancel, ex, userAccount)
    }
}