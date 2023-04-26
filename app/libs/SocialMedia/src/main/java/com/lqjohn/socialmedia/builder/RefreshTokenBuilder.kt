package com.lqjohn.socialmedia.builder

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.IRefreshToken
import com.lqjohn.socialmedia.model.UserAccount

abstract class RefreshTokenBuilder {
    protected lateinit var activity: FragmentActivity
    protected lateinit var callback: (newAccessToken: String?) -> Unit

    protected fun validate() {

    }

    fun withActivity(activity: FragmentActivity): RefreshTokenBuilder {
        this.activity = activity
        return this
    }

    fun withCallback(callback: (newAccessToken: String?) -> Unit): RefreshTokenBuilder {
        this.callback = callback
        return this
    }

    abstract fun build(): IRefreshToken
}