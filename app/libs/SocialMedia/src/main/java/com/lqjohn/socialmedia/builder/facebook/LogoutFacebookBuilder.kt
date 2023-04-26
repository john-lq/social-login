package com.lqjohn.socialmedia.builder.facebook

import com.lqjohn.socialmedia.builder.LogoutBuilder
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.facebook.LogoutFacebook

class LogoutFacebookBuilder : LogoutBuilder() {
    override fun build(): ILogout {
        validate()
        return LogoutFacebook(activity)
    }
}