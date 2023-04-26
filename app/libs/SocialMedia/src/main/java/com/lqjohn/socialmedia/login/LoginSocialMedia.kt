package com.lqjohn.socialmedia.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lqjohn.socialmedia.SocialMediaLoginManager
import com.lqjohn.socialmedia.model.UserAccount
import com.lqjohn.socialmedia.viewmodel.LoginViewModel

internal abstract class LoginSocialMedia(
    protected val activity: FragmentActivity,
    protected val isCancel: Observer<Boolean>,
    protected val ex: Observer<Exception>,
    protected val userAccount: Observer<UserAccount>
) : ILogin {
    companion object {
        private const val TAG = "Authentication"
    }

    private var mViewModel: LoginViewModel? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (mViewModel == null || requestCode != SocialMediaLoginManager.LOGIN_REQUEST_CODE)
            return

        if (resultCode != Activity.RESULT_OK) {
            mViewModel?.isCancel?.value = true
            return
        }

        if (intent == null) {
            mViewModel?.exception?.value = Exception("Response data is null")
            return
        }

        // Get data from the response intent
        val isSuccess = intent.getBooleanExtra(SocialMediaLoginManager.IS_SUCCESS, false)
        val exception: Exception? =
            intent.getSerializableExtra(SocialMediaLoginManager.EXCEPTION) as? Exception
        val data: UserAccount? =
            intent.getSerializableExtra(SocialMediaLoginManager.DATA) as? UserAccount

        if (!isSuccess || data == null)
            mViewModel?.exception?.value = exception
        else {
            mViewModel?.userAccount?.value = data
        }
    }

    protected fun setupViewModel(
        activity: FragmentActivity,
        isCancel: Observer<Boolean>,
        ex: Observer<Exception>,
        userAccount: Observer<UserAccount>
    ) {
        mViewModel = ViewModelProviders.of(activity).get(LoginViewModel::class.java)

        // Remove Observer
        removeObserve(mViewModel, activity)

        mViewModel?.isCancel?.observe(activity, isCancel)
        mViewModel?.exception?.observe(activity, ex)
        mViewModel?.userAccount?.observe(activity, userAccount)
    }

    protected fun setupViewModel(
        fragment: Fragment,
        isCancel: Observer<Boolean>,
        ex: Observer<Exception>,
        userAccount: Observer<UserAccount>
    ) {
        mViewModel = ViewModelProviders.of(fragment).get(LoginViewModel::class.java)

        // Remove Observer
        removeObserve(mViewModel, fragment)

        mViewModel?.isCancel?.observe(fragment, isCancel)
        mViewModel?.exception?.observe(fragment, ex)
        mViewModel?.userAccount?.observe(fragment, userAccount)
    }

    protected fun removeObserve(viewModel: LoginViewModel?, activity: FragmentActivity) {
        try {
            viewModel?.isCancel?.removeObservers(activity)
            viewModel?.exception?.removeObservers(activity)
            viewModel?.userAccount?.removeObservers(activity)

            viewModel?.isCancel?.value = null
            viewModel?.exception?.value = null
            viewModel?.userAccount?.value = null
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to remove observe. Error: ${ex.message}")
        }
    }

    protected fun removeObserve(viewModel: LoginViewModel?, fragment: Fragment) {
        try {
            viewModel?.isCancel?.removeObservers(fragment)
            viewModel?.exception?.removeObservers(fragment)
            viewModel?.userAccount?.removeObservers(fragment)

            viewModel?.isCancel?.value = null
            viewModel?.exception?.value = null
            viewModel?.userAccount?.value = null
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to remove observe. Error: ${ex.message}")
        }
    }
}