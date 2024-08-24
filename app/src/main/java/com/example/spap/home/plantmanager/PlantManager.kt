package com.example.spap.home.plantmanager

import GridSpacingItemDecoration
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.spap.R
import com.example.spap.databinding.FragmentPlantManagerBinding
import com.google.firebase.auth.FirebaseAuth


class PlantManager : Fragment() {
    private var _binding: FragmentPlantManagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlantManagerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlantManagerBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PlantManagerViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setRecyclerView()
        binding.addPlantBtn.setOnClickListener {
            addPlantClick()
        }


        return binding.root
    }

    private fun setRecyclerView() {
        val email = FirebaseAuth.getInstance().currentUser?.email
        email?.let {
            viewModel.plantForUser(it)
        }

        val adapter = PlantManagerAdapter(emptyList()) { plant ->
            val intent = Intent(requireContext(), PlantInfoManager::class.java)
            intent.putExtra("plant", plant)
            startActivity(intent)
        }

        binding.myPlantView.layoutManager = GridLayoutManager(requireContext(), 2)
        // ItemDecoration 설정
        val spacing =
            resources.getDimensionPixelSize(R.dimen.item_spacing) // dimensions.xml에 정의된 간격 사용
        binding.myPlantView.addItemDecoration(GridSpacingItemDecoration(spacing))

        binding.myPlantView.adapter = adapter

        viewModel.plantInfo.observe(viewLifecycleOwner) { plants ->
            val adapter = binding.myPlantView.adapter as? PlantManagerAdapter
            adapter?.updateList(plants)
        }
    }

    private fun addPlantClick() {
        val intent = Intent(requireActivity(), AddPlant::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}