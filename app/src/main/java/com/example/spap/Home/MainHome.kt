package com.example.spap.Home

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spap.databinding.ActivityMainHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
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

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}