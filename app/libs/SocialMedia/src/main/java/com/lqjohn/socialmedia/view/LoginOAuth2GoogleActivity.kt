package com.lqjohn.socialmedia.view

import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.viewmodel.LoginOAuth2GoogleViewModel


/**
 * https://github.com/openid/AppAuth-Android
 */
internal class LoginOAuth2GoogleActivity : SocialMediaActivity() {
    override val loginType: LoginType = LoginType.GOOGLE

    override fun setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(LoginOAuth2GoogleViewModel::class.java)
        (mViewModel as LoginOAuth2GoogleViewModel).onCreate(this)
        mViewModel.isCancel.observe(this, mIsCancelObserver)
        mViewModel.exception.observe(this, mExceptionObserver)
        mViewModel.userAccount.observe(this, mUserAccountObserver)
        mViewModel.signIn(this)
    }

    override fun onDestroy() {
        (mViewModel as LoginOAuth2GoogleViewModel).onDestroy()
        super.onDestroy()
    }
}
