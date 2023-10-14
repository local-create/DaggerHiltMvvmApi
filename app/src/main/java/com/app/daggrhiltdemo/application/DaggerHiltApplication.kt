package com.app.daggrhiltdemo.application

import android.app.Application
import com.app.daggrhiltdemo.repo.UserRepository
import com.app.daggrhiltdemo.session.SharedPreferenceHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DaggerHiltApplication: Application() {
}