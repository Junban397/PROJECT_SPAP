package com.example.spap.home.plantmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spap.R
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
        return binding.root
    }
}