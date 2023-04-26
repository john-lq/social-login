package com.lqjohn.socialmedia.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.lqjohn.socialmedia.R
import com.lqjohn.socialmedia.SocialMediaLoginManager
import com.lqjohn.socialmedia.model.LoginType
import com.lqjohn.socialmedia.model.UserAccount
import com.lqjohn.socialmedia.viewmodel.LoginViewModel

@Suppress("LeakingThis")
internal abstract class SocialMediaActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "SocialMediaActivity"
    }

    protected lateinit var mViewModel: LoginViewModel

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Activity life cycle methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_social_media)

        setupViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mViewModel.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun sendSuccessResponse(userAccount: UserAccount) {
        val intent = Intent()
        intent.putExtra(SocialMediaLoginManager.IS_SUCCESS, true)
        intent.putExtra(SocialMediaLoginManager.DATA, userAccount)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun sendCancelResponse() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun sendErrorResponse(ex: Exception?) {
        val intent = Intent()
        intent.putExtra(SocialMediaLoginManager.IS_SUCCESS, false)
        if (ex != null) {
            intent.putExtra(SocialMediaLoginManager.EXCEPTION, Exception(ex.toString()))
        }

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Observers
    ////////////////////////////////////////////////////////////////////////////////////////////////

    protected val mIsCancelObserver = Observer<Boolean> {
        if (it == true) {
            sendCancelResponse()
        }
    }

    protected val mExceptionObserver = Observer<Exception> {
        sendErrorResponse(it)
    }

    protected val mUserAccountObserver = Observer<UserAccount> {
        if (it != null) {
            sendSuccessResponse(it)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Abstract methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    abstract fun setupViewModel()

    abstract val loginType: LoginType
}
