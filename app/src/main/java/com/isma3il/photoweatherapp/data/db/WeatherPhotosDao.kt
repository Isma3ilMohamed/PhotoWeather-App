package com.isma3il.photoweatherapp.data.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface WeatherPhotosDao {

    @Query("SELECT * FROM weather_photos")
    fun getWeatherPhotos():Observable<List<WeatherPhotosEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(weatherPhotosEntity: WeatherPhotosEntity): Completable


}