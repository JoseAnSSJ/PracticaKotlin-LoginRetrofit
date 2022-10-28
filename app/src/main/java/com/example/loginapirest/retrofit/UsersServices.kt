package com.example.loginapirest.retrofit

import com.example.loginapirest.Constants
import retrofit2.http.GET

interface UsersServices {
    @GET(Constants.API_PATH + Constants.USERS_PATH + Constants.TWO_PATH)
    suspend fun getUser(): SingleUserResponse
}