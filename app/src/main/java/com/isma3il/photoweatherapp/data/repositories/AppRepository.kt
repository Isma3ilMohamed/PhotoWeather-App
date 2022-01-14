package com.isma3il.photoweatherapp.data.repositories

import com.isma3il.photoweatherapp.data.db.CacheHelper
import com.isma3il.photoweatherapp.data.db.database.WeatherPhotosEntity
import com.isma3il.photoweatherapp.data.network.NetworkHelper
import com.isma3il.photoweatherapp.data.network.model.WeatherMapper
import com.isma3il.photoweatherapp.domain.model.Weather
import com.isma3il.photoweatherapp.domain.model.WeatherPhoto
import com.isma3il.photoweatherapp.domain.repositories.Repository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val weatherMapper: WeatherMapper,
    private val cacheHelper: CacheHelper
) : Repository {
    override fun fetchWeatherInfo(lat: Double, lng: Double): Observable<Weather> {
        return networkHelper.getWeatherInfo(lat, lng).map {
            weatherMapper.map(it)
        }
    }

    override fun fetchHistoryPhotos(): Observable<List<WeatherPhoto>> {
        return cacheHelper.fetchWeatherPhotos().map {
            it.map {
                WeatherPhotosEntity.toDomain(it)
            }
        }
    }

    override fun addPhotoToHistory(weatherPhoto: WeatherPhoto): Completable {
        return cacheHelper.insertWeatherPhoto(WeatherPhotosEntity.fromDomain(weatherPhoto))
    }
}