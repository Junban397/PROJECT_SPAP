package com.example.spap.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.spap.BuildConfig
import com.example.spap.R
import com.example.spap.databinding.ActivityMainHomeBinding
import com.example.spap.home.plantmanager.PlantManager
import com.example.spap.home.tasks.TasksMain
import com.example.spap.home.tasks.WeatherApiClient
import com.example.spap.home.tasks.WeatherData
import com.example.spap.home.tasks.WeatherDataParser
import com.google.android.gms.location.LocationServices
import com.loopj.android.http.RequestParams
import org.json.JSONObject

class MainHome : AppCompatActivity() {
    private var _binding: ActivityMainHomeBinding? = null
    private val binding get() = _binding!!
    /*    private lateinit var navController: NavController*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 기본 프래그먼트 설정
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_main
            replaceFragment(TasksMain())
        }

        // BottomNavigationView 클릭 리스너 설정
        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_main -> {
                    replaceFragment(TasksMain())
                    true
                }

                R.id.nav_add_plant -> {
                    replaceFragment(PlantManager())
                    true

                }
                // 다른 메뉴 항목에 대한 경우 추가
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
