package com.lqjohn.socialmedia.builder.oauth2Google

import com.lqjohn.socialmedia.builder.LogoutBuilder
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.oauth2Google.LogoutOAuth2Google

class LogoutOAuth2GoogleBuilder : LogoutBuilder() {
    override fun build(): ILogout {
        validate()
        return LogoutOAuth2Google(activity)
    }
}