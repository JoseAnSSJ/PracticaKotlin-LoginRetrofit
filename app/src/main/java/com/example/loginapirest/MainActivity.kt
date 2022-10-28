package com.example.loginapirest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.loginapirest.databinding.ActivityMainBinding
import com.example.loginapirest.retrofit.LoginServices
import com.example.loginapirest.retrofit.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

/****
 * Project: Login API REST
 * From: com.cursosandroidant.loginapirest
 * Created by Alain Nicolás Tello on 07/02/22 at 1:10 PM
 * Course: Android Practical with Kotlin from zero.
 * Only on: https://www.udemy.com/course/kotlin-intensivo/
 * All rights reserved 2022.
 * My website: www.alainnicolastello.com
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.swType.setOnCheckedChangeListener { button, checked ->
            button.text = if (checked) getString(R.string.main_type_login)
            else getString(R.string.main_type_register)

            mBinding.btnLogin.text = button.text
        }

        mBinding.btnLogin.setOnClickListener {
            LoginOrRegistrer()
        }

        mBinding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun LoginOrRegistrer() {
        val email = mBinding.etEmail.text.toString().trim()
        val password = mBinding.etPassword.text.toString().trim()
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(LoginServices::class.java)
        if(mBinding.swType.isChecked){
            login(email, password,service)
        }else{
            registrer(email, password,service)
        }
    }

    private fun registrer(email: String, password: String, service: LoginServices) {
        lifecycleScope.launch{
            try {
                val result = service.registrerUser(UserInfo(email,password))
                updateUI("${Constants.ID_PROPERTY}: ${result.id}, ${Constants.TOKEN_PROPERTY}: ${result.token}")
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    checkError(e)
                }
            }
        }
    }


    private fun login(email: String, password: String, service: LoginServices) {
        lifecycleScope.launch(Dispatchers.IO){
            try {
                val result = service.loginUser(UserInfo(email,password))
                updateUI("${Constants.TOKEN_PROPERTY}: ${result.token}")
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    checkError(e)
                }
            }
        }
    }

    private suspend fun checkError(e: HttpException) {
        when(e.code()){
            400 ->{
                updateUI(getString(R.string.main_error_server))
            }
            else ->{
                updateUI(getString(R.string.main_error_response))
            }
        }
    }

    private suspend fun updateUI(result: String) {
        withContext(Dispatchers.Main) {
            mBinding.tvResult.visibility = View.VISIBLE
            mBinding.tvResult.text = result
        }
    }
}

/*service.login(UserInfo(email,password)).enqueue(
         object: Callback<LoginResponse>{
             override fun onResponse(
                 call: Call<LoginResponse>,
                 response: Response<LoginResponse>
             ) {
                 when(response.code()){
                     200 ->{
                         val result = response.body()
                         updateUI("${Constants.TOKEN_PROPERTY}: ${result?.token}")
                     }
                     400 ->{
                         updateUI(getString(R.string.main_error_server))
                     }
                     else ->{
                         updateUI(getString(R.string.main_error_response))
                     }
                 }
             }

             override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                 Log.e("error","Error en el servidor")
                     //updateUI(getString(R.string.main_error_server))

             }

         }
     )*/