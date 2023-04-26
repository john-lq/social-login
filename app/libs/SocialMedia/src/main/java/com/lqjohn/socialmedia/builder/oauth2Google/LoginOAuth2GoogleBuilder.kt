package com.lqjohn.socialmedia.builder.oauth2Google

import com.lqjohn.socialmedia.builder.LoginBuilder
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.oauth2Google.LoginOAuth2Google

class LoginOAuth2GoogleBuilder : LoginBuilder() {
    override fun build(): ILogin {
        validate()
        return LoginOAuth2Google(activity, isCancel, ex, userAccount)
    }
}