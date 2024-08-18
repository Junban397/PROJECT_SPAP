package com.example.spap.home.plantmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spap.R
import com.example.spap.databinding.ActivityAddPlantBinding
import com.example.spap.databinding.ActivityMainHomeBinding

class AddPlant : AppCompatActivity() {
    private var _binding:ActivityAddPlantBinding?=null
    private val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}