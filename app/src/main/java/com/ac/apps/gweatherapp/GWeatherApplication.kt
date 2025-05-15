package com.ac.apps.gweatherapp

import android.app.Application
import android.content.SharedPreferences
import com.ac.apps.gweatherapp.sharedpref.SharedPreferencesManager


/**
 *
 *
 * Created by   :   Alex Custodio on 15/05/2025.
 * Company      :   SMS Global Technologies, Inc.
 * Email        :   alex.custodio@smsgt.com
 *
 *
 **/
class GWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //initialize shared pref
        initializeSharedPreference()
    }

    private fun initializeSharedPreference(){
        SharedPreferencesManager.init(this)
    }
}