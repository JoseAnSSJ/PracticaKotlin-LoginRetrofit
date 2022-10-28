package com.example.loginapirest.retrofit

data class LoginResponse(var token: String): SuccessResponse(token)
