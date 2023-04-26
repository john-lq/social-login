package com.lqjohn.socialmedia.model

enum class LoginType(val type: Int) {
    NONE(-1),
    FACEBOOK(0),
    GOOGLE(1),
    TWITTER(2),
    UNSPLASH(3);

    fun getValue(): Int {
        return type
    }
}