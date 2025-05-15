package com.ac.apps.gweatherapp.dataclass


/**
 *
 *
 * Created by   :   Alex Custodio on 15/05/2025.
 * Company      :   SMS Global Technologies, Inc.
 * Email        :   alex.custodio@smsgt.com
 *
 *
 **/
data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val name: String,
    val sys: Sys
)

data class Weather(
    val description: String,
    val icon: String,
    val main: String,
)

data class Main(
    val temp: Double,
    val humidity: Int,
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)
