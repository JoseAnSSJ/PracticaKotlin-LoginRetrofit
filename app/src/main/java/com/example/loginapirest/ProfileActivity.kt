package com.example.loginapirest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.loginapirest.databinding.ActivityProfileBinding
import com.example.loginapirest.retrofit.LoginServices
import com.example.loginapirest.retrofit.UserInfo
import com.example.loginapirest.retrofit.UsersServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
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
class ProfileActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loadUserProfile()
    }

    private fun loadUserProfile() {

        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
        val service = retrofit.create(UsersServices::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = service.getUser()
                updateUI(result.data, result.support)
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    showMessage(getString(R.string.main_error_response))
                }
            }
        }


    }

    private suspend fun updateUI(user: User, support: Support) = withContext(Dispatchers.Main) {
        with(mBinding) {
            tvFullName.text = user.getFullName()
            tvEmail.text = user.email

            Glide.with(this@ProfileActivity)
                .load(user.avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(imgProfile)

            tvSupportMessage.text = support.text
            tvSupportUrl.text = support.url
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}