package com.lqjohn.socialmedia.model

import android.annotation.SuppressLint
import android.content.Context
import net.openid.appauth.AuthState

internal class AppSharedPref private constructor(val context: Context) {
    private var mShare = context.getSharedPreferences("SocialMedia.config", Context.MODE_PRIVATE)

    private fun setString(key: String, value: String) {
        val editor = mShare.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getString(key: String, defaultValue: String): String {
        return mShare.getString(key, defaultValue)
    }

    private fun setBoolean(key: String, value: Boolean) {
        val editor = mShare.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mShare.getBoolean(key, defaultValue)
    }

    @Synchronized
    fun getAuthState(): AuthState? {
        return try {
            AuthState.jsonDeserialize(getString(AUTH_STATE, ""))
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    @Synchronized
    fun setAuthState(authState: AuthState?) {
        setString(AUTH_STATE, authState?.jsonSerializeString() ?: "")
    }

    @Synchronized
    fun getUnsplashAuthState(): AuthState? {
        return try {
            AuthState.jsonDeserialize(getString(UNSPLASH_AUTH_STATE, ""))
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    @Synchronized
    fun setUnsplashAuthState(authState: AuthState?) {
        setString(UNSPLASH_AUTH_STATE, authState?.jsonSerializeString() ?: "")
    }

    companion object {
        // The keys can declare here
        private const val AUTH_STATE = "AUTH_STATE"
        private const val UNSPLASH_AUTH_STATE = "UNSPLASH_AUTH_STATE"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppSharedPref? = null

        fun getInstance(context: Context): AppSharedPref =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: AppSharedPref(context.applicationContext).also { INSTANCE = it }
                }
    }
}