package com.example.loginapirest.retrofit

import com.example.loginapirest.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginServices {
    @Headers("Content-Type: application/json")
    @POST(Constants.API_PATH + Constants.LOGIN_PATH)
    fun login(@Body data: UserInfo): Call<LoginResponse>


    @POST(Constants.API_PATH + Constants.LOGIN_PATH)
    suspend fun loginUser(@Body data: UserInfo): LoginResponse

    @POST(Constants.API_PATH + Constants.REGISTER_PATH)
    suspend fun registrerUser(@Body data: UserInfo): RegistreResponse
}