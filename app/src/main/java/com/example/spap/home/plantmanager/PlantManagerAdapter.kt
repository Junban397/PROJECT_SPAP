package com.example.spap.home.plantmanager

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spap.R
import com.example.spap.data.Plant
import com.example.spap.databinding.ItemPlantManagerBinding

class PlantManagerAdapter(
    private var plants: List<Plant>,
    private val onItemClicked: (Plant) -> Unit
) :
    RecyclerView.Adapter<PlantManagerAdapter.PlantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantManagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plants[position])
    }

    override fun getItemCount() = plants.size

    inner class PlantViewHolder(private val binding: ItemPlantManagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: Plant) {
            binding.plantInfo =plant
            binding.executePendingBindings()

            itemView.setOnClickListener {
                Log.d("PlantManagerAdapter", "Item clicked: ${plant.plantName}")
                onItemClicked(plant)
            }
        }
    }

    fun updateList(updatePlants: List<Plant>) {
        plants = updatePlants
        notifyDataSetChanged() // 데이터가 변경될 때마다 이 메서드를 호출하여 UI를 갱신합니다
    }


}