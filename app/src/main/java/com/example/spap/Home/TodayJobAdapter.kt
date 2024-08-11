package com.example.spap.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spap.data.CombinedPlantEvent
import com.example.spap.data.PlantEvent
import com.example.spap.databinding.TodayWorkItemBinding

class TodayJobAdapter : RecyclerView.Adapter<TodayJobAdapter.Holder>() {

    private val plantEventList = mutableListOf<CombinedPlantEvent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = TodayWorkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(plantEventList[position])
    }

    override fun getItemCount(): Int = plantEventList.size

    fun submitList(events: List<CombinedPlantEvent>) {
        plantEventList.clear()
        plantEventList.addAll(events)
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: TodayWorkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: CombinedPlantEvent) {
            binding.plantEvent = event
            binding.executePendingBindings() // 즉시 데이터 바인딩을 수행
        }
    }
}