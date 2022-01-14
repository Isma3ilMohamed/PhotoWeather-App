package com.isma3il.photoweatherapp.data.db

import com.isma3il.photoweatherapp.data.db.database.WeatherPhotosEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface CacheHelper {

    fun fetchWeatherPhotos():Observable<List<WeatherPhotosEntity>>

    fun insertWeatherPhoto(entity: WeatherPhotosEntity): Completable

}