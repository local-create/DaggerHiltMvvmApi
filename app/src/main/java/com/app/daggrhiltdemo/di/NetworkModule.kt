package com.app.daggrhiltdemo.di

import android.content.Context
import android.util.Log
import com.app.daggrhiltdemo.annotations.HeaderRestApi
import com.app.daggrhiltdemo.annotations.HeaderRetro
import com.app.daggrhiltdemo.annotations.SimpleRestApi
import com.app.daggrhiltdemo.annotations.SimpleRetro
import com.app.daggrhiltdemo.application.DaggerHiltApplication
import com.app.daggrhiltdemo.retrofit.RestApi
import com.app.daggrhiltdemo.session.SharedPreferenceHelper
import com.app.daggrhiltdemo.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
//import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    @SimpleRetro
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor { s -> Log.e("RetroClient", "requestBody: $s") }
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            chain.proceed(chain.request().newBuilder().build())
        }).addInterceptor(interceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS).build()
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    @HeaderRetro
    fun provideHeaderRetrofit(@ApplicationContext context: Context): Retrofit {
        val interceptor = HttpLoggingInterceptor { s -> Log.e("RetroClient", "requestBody: $s") }
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            chain.proceed(chain.request().newBuilder()
                .header("Authorization", "Bearer ${SharedPreferenceHelper(context).getValue("token")}").build())
        }).addInterceptor(interceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS).build()
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    @SimpleRestApi
    fun provideRestApi(@SimpleRetro retrofit: Retrofit) : RestApi{
        return retrofit.create(RestApi::class.java)
    }

    @Singleton
    @Provides
    @HeaderRestApi
    fun provideHeaderRestApi(@HeaderRetro retrofit: Retrofit) : RestApi{
        return retrofit.create(RestApi::class.java)
    }

    @Singleton
    @Provides
    fun getContext(@ApplicationContext context: Context): Context{
        return context
    }
}