package com.lqjohn.socialmedia.builder

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.login.ILogout
import com.lqjohn.socialmedia.model.UserAccount

abstract class LogoutBuilder {
    protected lateinit var activity: FragmentActivity
    protected lateinit var userAccount: Observer<UserAccount>

    protected fun validate() {

    }

    fun withActivity(activity: FragmentActivity): LogoutBuilder {
        this.activity = activity
        return this
    }

    abstract fun build(): ILogout
}