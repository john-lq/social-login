package com.lqjohn.socialmedia.view

import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.viewmodel.LoginGoogleViewModel


/**
 * https://developers.google.com/identity/sign-in/android/start-integrating
 * https://developers.google.com/identity/sign-in/android/sign-in
 */
internal class LoginGoogleActivity : SocialMediaActivity() {
    override val loginType: LoginType = LoginType.GOOGLE

    override fun setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(LoginGoogleViewModel::class.java)
        (mViewModel as LoginGoogleViewModel).onCreate(this)
        mViewModel.isCancel.observe(this, mIsCancelObserver)
        mViewModel.exception.observe(this, mExceptionObserver)
        mViewModel.userAccount.observe(this, mUserAccountObserver)
        mViewModel.signIn(this)
    }
}
