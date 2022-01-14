package com.isma3il.photoweatherapp.data.db

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface CacheHelper {

    fun fetchWeatherPhotos():Observable<List<WeatherPhotosEntity>>

    fun insertWeatherPhoto(entity: WeatherPhotosEntity): Completable

}