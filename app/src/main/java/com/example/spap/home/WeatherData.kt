package com.example.spap.home

import androidx.annotation.DrawableRes

class WeatherData {
    lateinit var tempString: String
    @DrawableRes var icon: Int = 0
    lateinit var weatherType: String
    lateinit var cityName: String
    var humidity: Int = 0
    var weatherId: Int = 0
    var tempInt: Int = 0
}