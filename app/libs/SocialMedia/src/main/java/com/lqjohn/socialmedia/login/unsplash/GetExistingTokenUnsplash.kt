package com.lqjohn.socialmedia.login.unsplash

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.login.IGetExistingToken
import com.lqjohn.socialmedia.viewmodel.LoginUnsplashViewModel

internal class GetExistingTokenUnsplash(private val activity: FragmentActivity, private val callback: (newToken: String?) -> Unit) : IGetExistingToken {
    override fun getExistingToken() {
        return try {
            ViewModelProviders.of(activity).get(LoginUnsplashViewModel::class.java).let {
                it.onCreate(activity)
                callback(it.getExistToken())
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            callback(null)
        }
    }
}