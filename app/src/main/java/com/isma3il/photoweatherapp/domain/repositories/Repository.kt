package com.isma3il.photoweatherapp.domain.repositories

import com.isma3il.photoweatherapp.domain.model.data.Weather
import com.isma3il.photoweatherapp.domain.model.data.WeatherPhoto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface Repository {

    fun fetchWeatherInfo(lat:Double,lng:Double):Observable<Weather?>

    fun fetchHistoryPhotos():Observable<List<WeatherPhoto>>

    fun addPhotoToHistory(weatherPhoto: WeatherPhoto):Completable
}