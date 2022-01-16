package com.isma3il.photoweatherapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isma3il.core.utils.loadImage
import com.isma3il.photoweatherapp.databinding.ItemPhotoBinding
import com.isma3il.photoweatherapp.domain.model.data.WeatherPhoto

class WeatherPhotoAdapter(private val callback: WeatherPhotoCallback) : RecyclerView.Adapter<WeatherPhotoAdapter.WeatherPhotoHolder>() {

    private val dataSet: MutableList<WeatherPhoto> = mutableListOf()

    inner class WeatherPhotoHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivWeatherPhoto.setOnClickListener {
                callback.onPhotoClick(dataSet[adapterPosition])
            }
        }

        fun bind(item: WeatherPhoto) {
            binding.ivWeatherPhoto.loadImage(item.photoPath)
        }
    }

    fun addData(photos:List<WeatherPhoto>){
        dataSet.clear()
        dataSet.addAll(photos)
        notifyItemRangeChanged(0,dataSet.size-1)
    }
    fun isEmpty():Boolean{
        return dataSet.isEmpty()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherPhotoHolder {
        return WeatherPhotoHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherPhotoHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}