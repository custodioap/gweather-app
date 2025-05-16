# GWeatherApp

This WeatherApp connects to the OpenWeather API to provide current weather information and a history of previously fetched weather data.

---

## Features

- **User Registration and Sign-In**  
- **Two Tabs:**  
  - **Current Weather:** Displays weather for the user's current location (city and country).  
  - **Weather History:** Shows a list of previously fetched weather entries each time the app is opened.  

- **Current Weather Display Includes:**  
  - Current location (City and Country)  
  - Current temperature in Celsius  
  - Time of sunrise and sunset  
  - Weather icon indicating current conditions:  
    - Sun icon during daytime  
    - Moon icon if time is past 6 PM  
    - Rain icon if it is raining  

---

## Setup Instructions

1. **Obtain OpenWeather API Key**  
   Register for a free API key at [OpenWeather](https://openweathermap.org/api).

2. **Add Your API Key in `local.properties`**  
   To keep your API key secure, add it to your project's `local.properties` file (create it if it doesn't exist) with the following line:
```kotlin  
openweather_api_key=your_api_key
```
3. **Add this on your `build.gradle.kts` app and call the method in `buildTypes`**
```kotlin
fun loadApiKey(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty("openweather_api_key") ?: ""
}

buildConfigField("String", "API_KEY", "\"${loadApiKey()}\"")
```

4. **Access the API Key in Your Code**  
Retrieve the API key in your code using Gradle properties, for example:  
```kotlin
val apiKey = BuildConfig.API_KEY
```


## Authors

- [@custodioap](https://github.com/users/custodioap)

