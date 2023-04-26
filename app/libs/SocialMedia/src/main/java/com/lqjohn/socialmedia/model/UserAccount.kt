package com.lqjohn.socialmedia.model

import java.io.Serializable

class UserAccount : Serializable {
    var id: String? = null
    var name: String? = null
    var email: String? = null
    var token: String? = null
    var gender: String? = null
    var errorMessage: String? = null
    var avatarUrl: String? = null
    var loginType: LoginType = LoginType.NONE

}