package com.lqjohn.socialmedia.builder.oauth2Google

import com.lqjohn.socialmedia.builder.RefreshTokenBuilder
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.IRefreshToken
import com.lqjohn.socialmedia.login.oauth2Google.RefreshTokenOAuth2Google

class RefreshTokenOAuth2GoogleBuilder : RefreshTokenBuilder() {
    override fun build(): IRefreshToken {
        validate()
        return RefreshTokenOAuth2Google(activity, callback)
    }
}