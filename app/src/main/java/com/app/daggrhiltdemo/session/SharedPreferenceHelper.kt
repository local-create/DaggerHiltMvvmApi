package com.app.daggrhiltdemo.session

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager

class SharedPreferenceHelper(context: Context) {
    val sharedPreferences: SharedPreferences
    val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences("daggerdemo", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun getValue(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }

    fun setValue(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
        editor.apply()
    }

    fun clear() {
        editor.clear()
        editor.commit()
        editor.apply()
    }
    companion object {
        const val loggedIn = "loggedIn"
        const val device_token = "device_token"
        const val token = "token"
        const val deviceId = "deviceId"
        const val mobile = "mobile"
        const val lastName = "lastName"
        const val deviceType = "deviceType"
        const val picture = "picture"
        var deviceToken = "deviceToken"
        const val id = "id"
        const val firstName = "firstName"
        const val loginBy = "loginBy"
        const val email = "email"
        const val address="address"
        const val driverStatus="driverStatus"
        const val requesterName = "requesterName"
        const val requesterImage = "requesterImage"
        const val notificationId = "notificationId"
        const val type = "type"
        const val km_price="km_price"
        const val day_price="day_price"
        const val discount="discount"
        const val min_charge="min_charge"
        const val service_type="service_type"
        const val update_status="false"
        const val login_status="false"
        const val otp_token="otp_token"
        const val login_through="login_through"
        const val notification_handler="notification_handler"
        const val notification_message="notification_message"
        const val defaultAddress="defaultAddress"
        const val isOneTimeFreeDone="false"
        fun isConnectingToInternet(context: Context): Boolean {
            val connectivity =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivity.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }

        fun hideKeyboard(activity: Activity) {
            val view: View =
                (if (activity.currentFocus == null) View(activity) else activity.currentFocus)!!
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}