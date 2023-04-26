package com.lqjohn.socialmedia.view

import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.viewmodel.LoginFacebookViewModel

/**
 * https://developers.facebook.com/docs/facebook-login/android/?sdk=maven
 * https://developers.facebook.com/docs/facebook-login/permissions/v3.0#permissions
 */
internal class LoginFacebookActivity : SocialMediaActivity() {
    override val loginType: LoginType = LoginType.FACEBOOK

    override fun setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(LoginFacebookViewModel::class.java)
        mViewModel.isCancel.observe(this, mIsCancelObserver)
        mViewModel.exception.observe(this, mExceptionObserver)
        mViewModel.userAccount.observe(this, mUserAccountObserver)
        mViewModel.signIn(this)
    }
}
