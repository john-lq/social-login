package com.lqjohn.socialmedia.builder

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.lqjohn.socialmedia.login.ILogin
import com.lqjohn.socialmedia.model.UserAccount

abstract class LoginBuilder {
    protected lateinit var activity: FragmentActivity
    protected lateinit var isCancel: Observer<Boolean>
    protected lateinit var ex: Observer<Exception>
    protected lateinit var userAccount: Observer<UserAccount>

    protected fun validate() {

    }

    fun withActivity(activity: FragmentActivity): LoginBuilder {
        this.activity = activity
        return this
    }

    fun withHandleCancel(isCancel: Observer<Boolean>): LoginBuilder {
        this.isCancel = isCancel
        return this
    }

    fun withHandleException(ex: Observer<Exception>): LoginBuilder {
        this.ex = ex
        return this
    }

    fun withHandleSuccess(userAccount: Observer<UserAccount>): LoginBuilder {
        this.userAccount = userAccount
        return this
    }

    abstract fun build(): ILogin
}