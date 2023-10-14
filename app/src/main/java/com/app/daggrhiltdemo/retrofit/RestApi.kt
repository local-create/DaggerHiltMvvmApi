package com.app.daggrhiltdemo.retrofit

import com.app.daggrhiltdemo.models.LoginResponse
import com.app.daggrhiltdemo.models.UserDetailResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestApi {

    @POST("login")
    fun login(@Body jsonObject: JsonObject) : Observable<LoginResponse>

    @GET("profile")
    fun getProfileDetails() : Observable<UserDetailResponse>
}