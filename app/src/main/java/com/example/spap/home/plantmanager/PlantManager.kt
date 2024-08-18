package com.example.spap.home.plantmanager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spap.R
import com.example.spap.Signup_login.Sign_up
import com.example.spap.databinding.FragmentPlantManagerBinding
import com.example.spap.databinding.FragmentTasksMainBinding


class PlantManager : Fragment() {
    private var _binding: FragmentPlantManagerBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlantManagerBinding.inflate(inflater, container, false)

        binding.addPlantBtn.setOnClickListener {
            addPlantClick()
        }

        return binding.root
    }
    private fun addPlantClick(){
        val intent = Intent(requireActivity(), AddPlant::class.java)
        startActivity(intent)
    }


}