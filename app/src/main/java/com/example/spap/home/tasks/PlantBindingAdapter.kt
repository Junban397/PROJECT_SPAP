package com.example.spap.home.tasks

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spap.data.CombinedPlantEvent

object PlantBindingAdapter {
    // RecyclerView에 데이터를 설정하는 BindingAdapter
    @BindingAdapter("items")
    @JvmStatic
    fun RecyclerView.setItems(items: List<CombinedPlantEvent>?) {
        if (this.adapter == null) {
            this.adapter = TodayJobAdapter()
        }
        items?.let {
            val adapter = this.adapter as TodayJobAdapter
            adapter.submitList(it.toMutableList())
        }
    }

    // ImageView에 이미지를 설정하는 BindingAdapter
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun ImageView.setImageUrl(imageUrl: String?) {
        Glide.with(this.context)
            .load(imageUrl)
            .override(200, 200)
            .circleCrop()
            .into(this)
    }
}