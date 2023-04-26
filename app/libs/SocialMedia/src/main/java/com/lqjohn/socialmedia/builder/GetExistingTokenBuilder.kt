package com.lqjohn.socialmedia.builder

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.lqjohn.socialmedia.login.IGetExistingToken
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.login.IRefreshToken
import com.lqjohn.socialmedia.model.UserAccount

abstract class GetExistingTokenBuilder {
    protected lateinit var activity: FragmentActivity
    protected lateinit var callback: (token: String?) -> Unit

    protected fun validate() {

    }

    fun withActivity(activity: FragmentActivity): GetExistingTokenBuilder {
        this.activity = activity
        return this
    }

    fun withCallback(callback: (token: String?) -> Unit): GetExistingTokenBuilder {
        this.callback = callback
        return this
    }

    abstract fun build(): IGetExistingToken
}