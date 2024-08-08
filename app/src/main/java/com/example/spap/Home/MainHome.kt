package com.example.spap.Home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.spap.R
import com.example.spap.Weather.LocationAndTime
import com.example.spap.Weather.WEATHER
import com.example.spap.Weather.WeatherObject
import com.example.spap.databinding.ActivityMainHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainHome : AppCompatActivity() {

    private var _binding: ActivityMainHomeBinding? = null
    private val binding get() = _binding!!
    private var baseDate = "20240807"
    private var baseTime = "1030"
    private var point: Point? = null
    private val locationAndTime = LocationAndTime()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 위치 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                // 현재 위치의 위경도를 격자 좌표로 변환
                point = locationAndTime.dfsXyConv(it.latitude, it.longitude)

                // 오늘 날짜 텍스트뷰 설정
                binding.todayDate.text = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Calendar.getInstance().time) + " 날씨"
                // nx, ny 지점의 날씨 가져와서 설정하기
                setWeather(point!!.x.toString(), point!!.y.toString())
            }
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            Toast.makeText(this, "위치 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setWeather(nx: String, ny: String) {
        val cal = Calendar.getInstance()
        baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time)
        val timeM = SimpleDateFormat("mm", Locale.getDefault()).format(cal.time)



        baseTime = locationAndTime.getBaseTime(timeH, timeM)
        if (timeH == "00" && baseTime == "2330") {
            cal.add(Calendar.DATE, -1)
            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

        val call = WeatherObject.getRetrofitService().getWeather("12", "1", "JSON", baseDate, baseTime, nx, ny)
        Log.d("Weather Request", "Preparing to request weather data for nx=$nx, ny=$ny")
        Log.d("Weather Request", "baseDate: $baseDate, baseTime: $baseTime")
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    Log.d("API Response", "Weather Response Body: ${weatherResponse.toString()}")
                    val resultCode = weatherResponse?.response?.header?.resultCode
                    val resultMsg = weatherResponse?.response?.header?.resultMsg
                    Log.d("API Response", "Result Code: $resultCode, Result Message: $resultMsg")

                    val items = weatherResponse?.response?.body?.items?.item
                    if (items != null) {
                        var rainRatio = ""
                        var rainType = ""
                        var humidity = ""
                        var sky = ""
                        var temp = ""
                        for (i in items.indices) {
                            Log.d("API Response", "Item: ${items[i]}")
                            when (items[i].category) {
                                "POP" -> rainRatio = items[i].fcstValue
                                "PTY" -> rainType = items[i].fcstValue
                                "REH" -> humidity = items[i].fcstValue
                                "SKY" -> sky = items[i].fcstValue
                                "TMP" -> temp = items[i].fcstValue
                                else -> continue
                            }
                        }
                        viewWeather(rainRatio, rainType, humidity, sky, temp)
                    } else {
                        Log.e("API Response", "Items are null")
                        Toast.makeText(this@MainHome, "날씨 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Response", "Response not successful")
                    Log.e("API Response", "Error code: ${response.code()}")
                    Log.e("API Response", "Error message: ${response.message()}")
                    Toast.makeText(this@MainHome, "날씨 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
                Toast.makeText(this@MainHome, "API 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun viewWeather(
        rainRatio: String,
        rainType: String,
        humidity: String,
        sky: String,
        temp: String
    ) {
        binding.temperatures.text = temp+"°"
        binding.humidity.text = "$humidity%"
        when (rainType) {
            "0", "1", "2", "3", "4" -> {
                when (rainType) {
                    "0" -> binding.weatherImage.setImageResource(R.drawable.sun)
                    "1" -> binding.weatherImage.setImageResource(R.drawable.rain)
                    "2" -> binding.weatherImage.setImageResource(R.drawable.rainsnow)
                    "3" -> binding.weatherImage.setImageResource(R.drawable.snow)
                    "4" -> binding.weatherImage.setImageResource(R.drawable.shower)
                }
            }

            else -> {
                when (sky) {
                    "1" -> binding.weatherImage.setImageResource(R.drawable.sun)
                    "3" -> binding.weatherImage.setImageResource(R.drawable.cloudy)
                    "4" -> binding.weatherImage.setImageResource(R.drawable.cloudy)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation()
        } else {
            Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}