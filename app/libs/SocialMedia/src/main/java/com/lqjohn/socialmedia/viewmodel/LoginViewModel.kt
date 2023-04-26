package com.lqjohn.socialmedia.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Intent
import com.lqjohn.socialmedia.model.UserAccount

internal open class LoginViewModel : ViewModel() {
    var isCancel: MutableLiveData<Boolean> = MutableLiveData()
    var exception: MutableLiveData<Exception> = MutableLiveData()
    var userAccount: MutableLiveData<UserAccount> = MutableLiveData()

    open fun signIn(activity: Activity) {
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    open fun signOut() {
    }
}