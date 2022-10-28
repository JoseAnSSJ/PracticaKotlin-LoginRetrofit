package com.example.loginapirest.retrofit

data class RegistreResponse(var id: String, var token: String) :SuccessResponse(token)