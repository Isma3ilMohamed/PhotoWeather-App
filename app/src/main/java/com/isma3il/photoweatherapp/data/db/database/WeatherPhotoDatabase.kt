package com.isma3il.photoweatherapp.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherPhotosEntity::class], version = 1)
abstract class WeatherPhotoDatabase:RoomDatabase() {

    abstract fun weatherPhotosDao(): WeatherPhotosDao
}