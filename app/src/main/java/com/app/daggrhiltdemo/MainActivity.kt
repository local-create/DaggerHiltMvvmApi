package com.app.daggrhiltdemo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.app.daggrhiltdemo.base.BaseActivity
import com.app.daggrhiltdemo.databinding.ActivityMainBinding
import com.app.daggrhiltdemo.repo.UserRepository
import com.app.daggrhiltdemo.session.SharedPreferenceHelper
import com.app.daggrhiltdemo.utils.showToast
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userRepo: UserRepository

    private val base by lazy {
        BaseActivity(this)
    }

    private var onDoubleBackPress = false

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPreferenceHelper(this).setValue("abc", "Hiiiiii")

        binding.btnLogin.setOnClickListener {
            val jsonObject = JsonObject()
            jsonObject.addProperty("email", binding.etEmail.text.toString().trim().ifEmpty { "" })
            jsonObject.addProperty(
                "password",
                binding.etPassword.text.toString().trim().ifEmpty { "" })
            userRepo.login(jsonObject)
            base.showProgress()
        }
        loginObserver()
    }

    private fun loginObserver() {
        userRepo.loginState.observe(this) {
            base.hideProgress()
            when (it) {
                is UserRepository.LoginSuccess -> {
                    Log.d("LoginSuccess", "loginObserver: ${it.response}")

                    SharedPreferenceHelper(this).setValue("token", it.response.access_token)
                    SharedPreferenceHelper(this).setValue("isLogin", "true")
                    startActivity(Intent(this, MainActivity2::class.java))
                }

                else -> {
                    Log.d("LoginError", "loginObserver: error")
                    showToast("Email or password wrong")
                }
            }
        }
    }

    override fun onBackPressed() {
        if (onDoubleBackPress) {
            finishAffinity()
        }

        this.onDoubleBackPress = true
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_LONG).show()
        Handler(Looper.getMainLooper()).postDelayed({
            this.onDoubleBackPress = false
        }, 3000)
    }
}