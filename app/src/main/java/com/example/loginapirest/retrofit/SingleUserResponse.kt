package com.example.loginapirest.retrofit

import com.example.loginapirest.Support
import com.example.loginapirest.User

data class SingleUserResponse(
        var data: User,
        var support: Support
)
