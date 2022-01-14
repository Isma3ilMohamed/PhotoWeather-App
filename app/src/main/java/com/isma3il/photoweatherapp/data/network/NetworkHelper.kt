package com.isma3il.photoweatherapp.data.network

import com.isma3il.photoweatherapp.data.network.model.WeatherInfo
import io.reactivex.rxjava3.core.Observable

interface NetworkHelper {

    fun getWeatherInfo(lat: Double,lng: Double):Observable<WeatherInfo>
}