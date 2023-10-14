package com.app.daggrhiltdemo.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.daggrhiltdemo.annotations.HeaderRestApi
import com.app.daggrhiltdemo.annotations.SimpleRestApi
import com.app.daggrhiltdemo.models.LoginResponse
import com.app.daggrhiltdemo.models.UserDetailResponse
import com.app.daggrhiltdemo.retrofit.RestApi
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val TAG = "UserRepo"

class UserRepository @Inject constructor() {

    @Inject
    @SimpleRestApi
    lateinit var api: RestApi

    @Inject
    @HeaderRestApi
    lateinit var headerApi: RestApi

    private val compositeDisposable = CompositeDisposable()

    private var _loginState = MutableLiveData<LoginState>()
    var loginState: LiveData<LoginState> = _loginState

    private var _userDetailState = MutableLiveData<UserDetailState>()
    var userDetailState : LiveData<UserDetailState> = _userDetailState


    fun login(jsonObject: JsonObject) {
        compositeDisposable.add(api.login(jsonObject)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                Log.e("loginsubscribe", "$it")
                _loginState.postValue(LoginSuccess(it))
            }, {
                Log.e("loginerror", "${it.localizedMessage}")
                _loginState.postValue(LoginError)
            }
            ))
    }

    fun userDetails() {
        compositeDisposable.add(headerApi.getProfileDetails()
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                Log.e("userDetailsubscribe", "$it")
                _userDetailState.postValue(UserDetailSuccess(it))
            }, {
                Log.e("userDetailerror", "${it.localizedMessage}")
                _userDetailState.postValue(UserDetailError)
            }
            ))
    }


    sealed class LoginState
    class LoginSuccess(var response: LoginResponse) : LoginState()
    object LoginError : LoginState()

    sealed class UserDetailState
    class UserDetailSuccess(var response: UserDetailResponse) : UserDetailState()
    object UserDetailError : UserDetailState()

}