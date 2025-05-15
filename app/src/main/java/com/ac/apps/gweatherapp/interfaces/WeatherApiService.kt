package com.ac.apps.gweatherapp.interfaces

import com.ac.apps.gweatherapp.dataclass.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


/**
 *
 *
 * Created by   :   Alex Custodio on 15/05/2025.
 * Company      :   SMS Global Technologies, Inc.
 * Email        :   alex.custodio@smsgt.com
 *
 *
 **/
interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metrics"
    ): WeatherResponse
}