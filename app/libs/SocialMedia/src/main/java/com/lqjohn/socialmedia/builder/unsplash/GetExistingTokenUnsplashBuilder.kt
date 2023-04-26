package com.lqjohn.socialmedia.builder.unsplash

import com.lqjohn.socialmedia.builder.GetExistingTokenBuilder
import com.lqjohn.socialmedia.builder.LogoutBuilder
import com.lqjohn.socialmedia.login.IGetExistingToken
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.unsplash.GetExistingTokenUnsplash
import com.lqjohn.socialmedia.login.unsplash.LogoutUnsplash

class GetExistingTokenUnsplashBuilder : GetExistingTokenBuilder() {
    override fun build(): IGetExistingToken {
        validate()
        return GetExistingTokenUnsplash(activity, callback)
    }
}