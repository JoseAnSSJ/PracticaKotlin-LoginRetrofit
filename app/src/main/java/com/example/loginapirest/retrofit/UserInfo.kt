package com.example.loginapirest.retrofit

import com.example.loginapirest.Constants
import com.google.gson.annotations.SerializedName


class UserInfo(
    @SerializedName(Constants.EMAIL_PARAM)
    val email: String,
    @SerializedName(Constants.PASSWORD_PARAM)
    val password: String
)