package com.lqjohn.socialmedia.builder.facebook

import com.lqjohn.socialmedia.builder.LoginBuilder
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.facebook.LoginFacebook

class LoginFacebookBuilder : LoginBuilder() {
    override fun build(): ILogin {
        validate()
        return LoginFacebook(activity, isCancel, ex, userAccount)
    }
}