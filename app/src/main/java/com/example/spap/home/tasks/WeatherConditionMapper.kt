package com.example.spap.home.tasks

import androidx.annotation.DrawableRes
import com.example.spap.R

class WeatherConditionMapper {

    fun mapWeatherCondition(condition: String): Pair<String, Int> {
        @DrawableRes
        val iconRes = when (condition) {
            "Clear" -> R.drawable.sun
            "Clouds" -> R.drawable.cloudy
            "Rain" -> R.drawable.rain
            "Drizzle" -> R.drawable.rain
            "Thunderstorm" -> R.drawable.rain
            "Snow" -> R.drawable.snow
            "Mist" -> R.drawable.cloudy
            else -> R.drawable.sun
        }
        val description = when (condition) {
            "Clear" -> "맑음"
            "Clouds" -> "구름"
            "Rain" -> "비"
            "Drizzle" -> "이슬비"
            "Thunderstorm" -> "천둥번개"
            "Snow" -> "눈"
            "Mist" -> "안개"
            else -> "알 수 없음"
        }
        return description to iconRes
    }
}