package com.app.daggrhiltdemo.models

data class LoginResponse(
    val access_token: String,
    val refresh_token: String
)