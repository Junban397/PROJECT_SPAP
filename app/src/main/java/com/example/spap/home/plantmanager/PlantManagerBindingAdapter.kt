package com.example.spap.home.plantmanager

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spap.R
import com.example.spap.data.Plant

object PlantManagerBindingAdapter {
    @BindingAdapter("plantManagerItems")
    @JvmStatic
    fun RecyclerView.setPlantManagerItems(items: List<Plant>?) {
        if (this.adapter == null) {
            this.adapter = PlantManagerAdapter(emptyList()) {}
        }
        items?.let {
            val adapter = this.adapter as PlantManagerAdapter
            adapter.updateList(it)
        }
    }

    @BindingAdapter("plantManagerImageUrl")
    @JvmStatic
    fun ImageView.setPlantManagerImageUrl(imageUrl: String?) {
        Glide.with(this.context)
            .load(imageUrl ?: R.drawable.plant_icon) // imageUrl이 null일 때 기본 이미지 설정
            .placeholder(R.drawable.plant_icon)
            .into(this)
    }
}