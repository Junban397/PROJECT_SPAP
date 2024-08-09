package com.example.spap.Home

import org.json.JSONObject

class WeatherDataParser {

    fun parseWeatherData(jsonObject: JSONObject?): WeatherData? {
        return try {
            val weatherData = WeatherData()

            val weatherArray = jsonObject?.getJSONArray("weather")
            if (weatherArray != null && weatherArray.length() > 0) {
                val weatherObject = weatherArray.getJSONObject(0)
                weatherData.weatherId = weatherObject.getInt("id")
                val condition = weatherObject.getString("main")
                val (description, iconRes) = WeatherConditionMapper().mapWeatherCondition(condition)
                weatherData.weatherType = description
                weatherData.icon = iconRes
            }

            val mainObject = jsonObject?.getJSONObject("main")
            if (mainObject != null) {
                weatherData.humidity = mainObject.getInt("humidity")
                val tempK = mainObject.getDouble("temp")
                val tempC = (tempK - 273.15).toInt()
                weatherData.tempString = tempC.toString()
                weatherData.tempInt = tempC
            }

            weatherData.cityName = jsonObject?.getString("name") ?: ""
            weatherData

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}