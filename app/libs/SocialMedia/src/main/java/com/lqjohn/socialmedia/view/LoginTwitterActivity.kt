package com.lqjohn.socialmedia.view

import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.viewmodel.LoginTwitterViewModel
import com.twitter.sdk.android.core.Twitter


internal class LoginTwitterActivity : SocialMediaActivity() {
    override val loginType: LoginType = LoginType.TWITTER

    override fun setupViewModel() {
        // Initialize Twitter Kit
//        val config = TwitterConfig.Builder(this)
//                .logger(DefaultLogger(Log.DEBUG))
//                .twitterAuthConfig(TwitterAuthConfig(getString(R.string.consumer_key), getString(R.string.consumer_secret)))
//                .debug(true)
//                .build()
//        Twitter.initialize(config)
        Twitter.initialize(this)

        // Initialize view model
        mViewModel = ViewModelProviders.of(this).get(LoginTwitterViewModel::class.java)
        mViewModel.isCancel.observe(this, mIsCancelObserver)
        mViewModel.exception.observe(this, mExceptionObserver)
        mViewModel.userAccount.observe(this, mUserAccountObserver)
        mViewModel.signIn(this)
    }
}