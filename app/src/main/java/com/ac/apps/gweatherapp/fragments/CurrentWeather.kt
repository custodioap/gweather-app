package com.ac.apps.gweatherapp.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.ac.apps.gweatherapp.BuildConfig
import com.ac.apps.gweatherapp.R
import com.ac.apps.gweatherapp.dataclass.Weather
import com.ac.apps.gweatherapp.dataclass.WeatherResponse
import com.ac.apps.gweatherapp.sharedpref.SharedPreferencesManager
import com.ac.apps.gweatherapp.utils.AppConstants
import com.ac.apps.gweatherapp.viewmodels.WeatherViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.roundToInt


class CurrentWeather : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var currentBg: LinearLayout
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 60 * 1000 // 1 minute

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_current_weather, container, false)
        currentBg = rootView.findViewById(R.id.currentBg)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        updateBg()

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        weatherViewModel.weatherData.observe(viewLifecycleOwner) { weather ->
            updateUi(weather, view)
        }

        getCurrentLocationAndFetchWeather()

//        weatherViewModel.fetchWeather(lat = 14.529410, lon = 121.071022, BuildConfig.API_KEY)
    }



    override fun onResume() {
        super.onResume()
        startBgUpdater()
    }

    override fun onPause() {
        super.onPause()
        stopBgUpdater()
    }


    private fun updateUi(weather: WeatherResponse, view: View){
        val desc = weather.weather[0].description
        val temp = weather.main.temp
        val type = weather.weather[0].main
        val icon = weather.weather[0].icon
        val humid = weather.main.humidity
        val city = weather.name
        val country = weather.sys.country
        val sunrise = weather.sys.sunrise
        val sunset = weather.sys.sunset

        val convertedTemp = AppConstants.convertKelvinToCelsius(temp);
        val roundedTemp = convertedTemp.roundToInt()
        val sunriseDt = AppConstants.convertEpochToReadableFormat(sunrise)
        val sunsetDt = AppConstants.convertEpochToReadableFormat(sunset)

        val tvCity: TextView = view.findViewById(R.id.currentCity)
        val tvTemp: TextView = view.findViewById(R.id.currentTemp)
        val tvType: TextView = view.findViewById(R.id.currentWeather)
        val tvSunrise: TextView = view.findViewById(R.id.sunrise)
        val tvSunset: TextView = view.findViewById(R.id.sunset)
        val iconImg: ImageView = view.findViewById(R.id.currentIcon)

        val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"


        tvCity.text = "$city, $country"
        tvTemp.text = "$roundedTemp°C"
        tvType.text = desc
        tvSunrise.text = sunriseDt
        tvSunset.text = sunsetDt

        // set the icon for current weather
        Glide.with(this).load(iconUrl).into(iconImg)


        SharedPreferencesManager.saveWeather(city, roundedTemp.toString(), desc, humid.toString(), type)
        Log.d("Weather saved", "City: $city, Temp: $roundedTemp°C, Desc: $desc, Humidity: $humid%, Type: $type, Sunrise: $sunriseDt, Sunset: $sunsetDt")
    }

    private fun updateBg(){
        val calendar = Calendar.getInstance()
        val currHour = calendar.get(Calendar.HOUR_OF_DAY)

        currentBg.setBackgroundResource(if (currHour >= 18) R.drawable.night else R.drawable.after_noon)

    }

    private fun startBgUpdater(){
        handler.post(object : Runnable {
            override fun run() {
                updateBg()
                handler.postDelayed(this, updateInterval)
            }
            
        })
    }

    private fun stopBgUpdater(){
        handler.removeCallbacksAndMessages(null)
    }

    private fun getCurrentLocationAndFetchWeather() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not granted
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Fetch the last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                weatherViewModel.fetchWeather(lat = latitude, lon = longitude, BuildConfig.API_KEY)
            } else {
                // Handle the case where location is null
                Log.d("Location", "Unable to retrieve location.")
            }
        }.addOnFailureListener {
            Log.d("Location", "Failed to get location: ${it.message}")
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndFetchWeather()
            } else {
                Log.d("Permission", "Location permission denied.")
            }
        }
    }



}