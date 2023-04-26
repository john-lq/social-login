package com.lqjohn.socialmedia.builder.twitter

import com.lqjohn.socialmedia.builder.LoginBuilder
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.twitter.LoginTwitter

class LoginTwitterBuilder : LoginBuilder() {
    override fun build(): ILogin {
        validate()
        return LoginTwitter(activity, isCancel, ex, userAccount)
    }
}