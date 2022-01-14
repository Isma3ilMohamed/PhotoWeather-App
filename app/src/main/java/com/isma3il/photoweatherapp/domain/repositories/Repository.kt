package com.isma3il.photoweatherapp.domain.repositories

import com.isma3il.photoweatherapp.domain.model.Weather
import io.reactivex.rxjava3.core.Observable

interface Repository {

    fun fetchWeatherInfo(lat:Double,lng:Double):Observable<Weather>

}