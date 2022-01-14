package com.isma3il.photoweatherapp.data.repositories

import com.isma3il.photoweatherapp.data.network.NetworkHelper
import com.isma3il.photoweatherapp.data.network.model.WeatherMapper
import com.isma3il.photoweatherapp.domain.model.Weather
import com.isma3il.photoweatherapp.domain.repositories.Repository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val weatherMapper: WeatherMapper
) : Repository {
    override fun fetchWeatherInfo(lat: Double, lng: Double): Observable<Weather> {
        return networkHelper.getWeatherInfo(lat, lng).map {
            weatherMapper.map(it)
        }
    }
}