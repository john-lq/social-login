package com.lqjohn.socialmedia.builder.google

import com.lqjohn.socialmedia.builder.LogoutBuilder
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.google.LogoutGoogle

class LogoutGoogleBuilder : LogoutBuilder() {
    override fun build(): ILogout {
        validate()
        return LogoutGoogle(activity)
    }
}