package com.ac.apps.gweatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ac.apps.gweatherapp.dataclass.WeatherResponse
import com.ac.apps.gweatherapp.utils.AppConstants
import kotlinx.coroutines.launch
import retrofit2.Retrofit


/**
 *
 *
 * Created by   :   Alex Custodio on 15/05/2025.
 * Company      :   SMS Global Technologies, Inc.
 * Email        :   alex.custodio@smsgt.com
 *
 *
 **/
class WeatherViewModel : ViewModel(){
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> get() = _weatherData

    fun fetchWeather(lat: Double, lon: Double, apiKey: String){
        viewModelScope.launch {
            try {
                val response = AppConstants.api.getWeather(lat, lon, apiKey)
                _weatherData.postValue(response)
            } catch (e: Exception){
                Log.e("WeatherViewModel", "Error fetching weather: ${e.message}")
            }
        }
    }
}