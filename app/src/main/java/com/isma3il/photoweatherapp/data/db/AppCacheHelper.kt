package com.isma3il.photoweatherapp.data.db

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AppCacheHelper @Inject constructor(
    private val weatherPhotosDao: WeatherPhotosDao
) : CacheHelper {
    override fun fetchWeatherPhotos(): Observable<List<WeatherPhotosEntity>> {
        return weatherPhotosDao.getWeatherPhotos()
    }

    override fun insertWeatherPhoto(entity: WeatherPhotosEntity): Completable {
        return weatherPhotosDao.insertPhoto(entity)
    }


}