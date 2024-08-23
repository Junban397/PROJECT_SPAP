package com.example.spap.home.tasks

import TodayJobAdapter
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spap.R
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
            .placeholder(R.drawable.plant_icon) // 기본 이미지 리소스
            .override(300, 300) // 원하는 크기로 조정
            .circleCrop() // 원형으로 자르기
            .into(this)
    }
}