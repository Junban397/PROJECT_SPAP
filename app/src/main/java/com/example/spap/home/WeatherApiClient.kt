package com.example.spap.home

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class WeatherApiClient(private val apiKey: String, private val weatherUrl: String) {

    fun fetchWeather(params: RequestParams, callback: WeatherApiCallback) {
        val client = AsyncHttpClient()
        client.get(weatherUrl, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                callback.onSuccess(response)
            }

            override fun onFailure(
                statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?
            ) {
                callback.onFailure(throwable)
            }
        })
    }

    interface WeatherApiCallback {
        fun onSuccess(response: JSONObject?)
        fun onFailure(error: Throwable?)
    }
}