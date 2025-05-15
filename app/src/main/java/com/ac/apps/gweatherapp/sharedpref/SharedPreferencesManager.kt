package com.ac.apps.gweatherapp.sharedpref

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


/**
 *
 *
 * Created by   :   Alex Custodio on 15/05/2025.
 * Company      :   SMS Global Technologies, Inc.
 * Email        :   alex.custodio@smsgt.com
 *
 *
 **/
import org.json.JSONArray
import org.json.JSONObject

object SharedPreferencesManager {

    private const val PREF_NAME = "gweather_pref"
    private const val USERS_KEY = "users" // Key to store all users as a JSON array
    private const val WEATHER_KEY = "weather"
    private lateinit var encryptedPreferences: SharedPreferences

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encryptedPreferences = EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun putString(key: String, value: String) {
        encryptedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return encryptedPreferences.getString(key, defaultValue)
    }

    fun remove(key: String) {
        encryptedPreferences.edit().remove(key).apply()
    }

    fun clear() {
        encryptedPreferences.edit().clear().apply()
    }

    // Save a new user
    fun saveUser(username: String, password: String, email: String) {
        val usersArray = getUsersArray()

        // Check if user already exists
        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("username") == username) {
                // Update existing user data
                user.put("password", password)
                user.put("email", email)
                saveUsersArray(usersArray)
                return
            }
        }

        // Add new user
        val newUser = JSONObject()
        newUser.put("username", username)
        newUser.put("password", password) // Consider encrypting/hashing passwords
        newUser.put("email", email)
        usersArray.put(newUser)

        saveUsersArray(usersArray)
    }

    fun saveWeather(city: String, temp: String, desc: String, humid: String, type: String){
        val weatherArray = getWeatherArray()

        val newWeather = JSONObject()
        newWeather.put("city", city)
        newWeather.put("description", desc)
        newWeather.put("temperature", temp)
        newWeather.put("type", type)
        newWeather.put("humidity", humid)
        weatherArray.put(newWeather)

        saveWeatherArray(weatherArray)
    }

    // Get all users
    fun getAllUsers(): List<Map<String, String>> {
        val usersArray = getUsersArray()
        val usersList = mutableListOf<Map<String, String>>()

        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            val userData = mapOf(
                "username" to user.getString("username"),
                "password" to user.getString("password"),
                "email" to user.getString("email")
            )
            usersList.add(userData)
        }

        return usersList
    }

    // get All weather
    fun getAllWeather(): List<Map<String, String>> {
        val weatherArray = getWeatherArray()
        val weatherList = mutableListOf<Map<String, String>>()

        for (i in 0 until weatherArray.length()){
            val weather = weatherArray.getJSONObject(i)
            val weatherData = mapOf(
                "city" to weather.getString("city"),
                "temperature" to weather.getString("temperature"),
                "description" to weather.getString("description"),
                "humidity" to weather.getString("humidity")
            )

            weatherList.add(weatherData)
        }

        return weatherList
    }

    // Validate user login
    fun isValidUser(username: String, password: String): Boolean {
        val usersArray = getUsersArray()

        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("email") == username && user.getString("password") == password) {
                return true
            }
        }
        return false
    }

    // Private helper to get users as a JSONArray
    private fun getUsersArray(): JSONArray {
        val usersString = encryptedPreferences.getString(USERS_KEY, "[]")
        return JSONArray(usersString)
    }

    // Private helper for weather
    private fun getWeatherArray(): JSONArray{
        val weatherString = encryptedPreferences.getString(WEATHER_KEY, "[]")
        return JSONArray(weatherString)
    }

    // Private helper to save JSONArray to SharedPreferences
    private fun saveUsersArray(usersArray: JSONArray) {
        encryptedPreferences.edit()
            .putString(USERS_KEY, usersArray.toString())
            .apply()
    }

    private fun saveWeatherArray(weatherArray: JSONArray){
        encryptedPreferences.edit()
            .putString(WEATHER_KEY, weatherArray.toString())
            .apply()
    }
}
