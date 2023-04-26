package com.lqjohn.socialmedia.view

import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.viewmodel.LoginUnsplashViewModel


/**
 * https://github.com/openid/AppAuth-Android
 */
internal class LoginUnsplashActivity : SocialMediaActivity() {
    override val loginType: LoginType = LoginType.UNSPLASH

    override fun setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(LoginUnsplashViewModel::class.java)
        (mViewModel as LoginUnsplashViewModel).onCreate(this)
        mViewModel.isCancel.observe(this, mIsCancelObserver)
        mViewModel.exception.observe(this, mExceptionObserver)
        mViewModel.userAccount.observe(this, mUserAccountObserver)
        mViewModel.signIn(this)
    }

    override fun onDestroy() {
        (mViewModel as LoginUnsplashViewModel).onDestroy()
        super.onDestroy()
    }
}
