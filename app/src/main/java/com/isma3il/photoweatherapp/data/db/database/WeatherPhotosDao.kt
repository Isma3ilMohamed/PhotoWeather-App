package com.isma3il.photoweatherapp.data.db.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface WeatherPhotosDao {

    @Query("SELECT * FROM weather_photos")
    fun getWeatherPhotos():Observable<List<WeatherPhotosEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(weatherPhotosEntity: WeatherPhotosEntity): Completable

    @Delete
    fun deletePhoto(weatherPhotosEntity: WeatherPhotosEntity):Completable

}