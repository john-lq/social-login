package com.lqjohn.socialmedia.builder.google

import com.lqjohn.socialmedia.builder.LoginBuilder
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.google.LoginGoogle

class LoginGoogleBuilder : LoginBuilder() {
    override fun build(): ILogin {
        validate()
        return LoginGoogle(activity, isCancel, ex, userAccount)
    }
}