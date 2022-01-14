package com.isma3il.photoweatherapp.data.network

import com.isma3il.photoweatherapp.data.network.model.WeatherInfo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AppNetworkHelper@Inject constructor(
    private val api: Api
):NetworkHelper {
    override fun getWeatherInfo(lat: Double,lng: Double): Observable<WeatherInfo> {
        return api.getWeatherInfo(lat, lng)
    }
}