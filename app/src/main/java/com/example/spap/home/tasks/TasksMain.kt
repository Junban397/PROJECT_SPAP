package com.example.spap.home.tasks

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.spap.BuildConfig
import com.example.spap.R
import com.example.spap.databinding.FragmentSignupNameBinding
import com.example.spap.databinding.FragmentTasksMainBinding
import com.example.spap.home.MainHome
import com.google.android.gms.location.LocationServices
import com.loopj.android.http.RequestParams
import org.json.JSONObject


class TasksMain : Fragment() {
    private var _binding: FragmentTasksMainBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val API_KEY: String = BuildConfig.openWeatherApi
        const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather"
        private const val REQUEST_LOCATION = 1
        private const val TAG = "WeatherDebug"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksMainBinding.inflate(inflater, container, false)
        requestLocation()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun requestLocation() {
        val activity = requireActivity()
        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION
            )
        } else {
            getWeatherInCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getWeatherInCurrentLocation() {
        val activity = requireActivity()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val params = RequestParams().apply {
                        put("lat", it.latitude)
                        put("lon", it.longitude)
                        put("appid", API_KEY)
                        put("lang", "kr")
                    }
                    fetchWeatherData(params)
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to obtain location", it)
                Toast.makeText(activity, "위치 정보를 제공받지못함", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchWeatherData(params: RequestParams) {
        val weatherApiClient = WeatherApiClient(API_KEY, WEATHER_URL)
        weatherApiClient.fetchWeather(params, object : WeatherApiClient.WeatherApiCallback {
            override fun onSuccess(response: JSONObject?) {
                val weatherData = WeatherDataParser().parseWeatherData(response)
                weatherData?.let {
                    updateWeather(it)
                }
            }

            override fun onFailure(error: Throwable?) {
                Log.e(TAG, "Failed to fetch weather data", error)
            }
        })
    }

    private fun updateWeather(weather: WeatherData) {
        binding.temperatures.text = weather.tempString+"°C"
        binding.humidity.text = weather.humidity.toString()+"%"
        binding.region.text = weather.cityName
        binding.weatherType.text = weather.weatherType
        binding.weatherIcon.setImageResource(weather.icon)
    }

}