package com.lqjohn.socialmedia.builder.unsplash

import com.lqjohn.socialmedia.builder.LogoutBuilder
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.unsplash.LogoutUnsplash

class LogoutUnsplashBuilder : LogoutBuilder() {
    override fun build(): ILogout {
        validate()
        return LogoutUnsplash(activity)
    }
}