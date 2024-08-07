package com.example.spap.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spap.R
import com.example.spap.databinding.ActivityMainHomeBinding
import com.example.spap.databinding.FragmentSignupNameBinding
import com.example.spap.databinding.FragmentSignupPasswordBinding

class MainHome : AppCompatActivity() {

    private var _binding: ActivityMainHomeBinding? = null
    private val binding get() = _binding!!

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
