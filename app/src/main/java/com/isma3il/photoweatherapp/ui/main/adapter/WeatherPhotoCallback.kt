package com.isma3il.photoweatherapp.ui.main.adapter

import com.isma3il.photoweatherapp.domain.model.data.WeatherPhoto

interface WeatherPhotoCallback {
    fun onPhotoClick(photo: WeatherPhoto)
}