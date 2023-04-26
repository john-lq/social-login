package com.lqjohn.socialmedia.builder.twitter

import com.lqjohn.socialmedia.builder.LogoutBuilder
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.twitter.LogoutTwitter

class LogoutTwitterBuilder : LogoutBuilder() {
    override fun build(): ILogout {
        validate()
        return LogoutTwitter(activity)
    }
}