package com.lqjohn.socialmedia.login

import android.content.Intent

interface ILogin {
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?)
    fun login()
}