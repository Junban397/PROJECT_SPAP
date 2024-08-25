package com.example.spap.home.plantmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.spap.R
import com.example.spap.data.Plant
import com.example.spap.databinding.ActivityPlantInfoBinding
import com.example.spap.home.plantmanager.PlantManagerBindingAdapter.setPlantManagerImageUrl

class PlantInfoManager : AppCompatActivity() {
    private var _binding: ActivityPlantInfoBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlantInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val plant = intent.getSerializableExtra("plant") as? Plant
        binding.plantNameInfo.text=plant?.plantName
        binding.plantTypeInfo.text=plant?.plantType
        binding.plantTemp.text=plant?.plantTemp
        binding.plantHumidity.text=plant?.plantHumidity
        binding.plantSoil.text=plant?.plantRepottedFrequency
        binding.plantWater.text=plant?.plantWateringFrequency
        binding.plantImageInfo.setPlantManagerImageUrl(plant?.imageUrl)

    }
}