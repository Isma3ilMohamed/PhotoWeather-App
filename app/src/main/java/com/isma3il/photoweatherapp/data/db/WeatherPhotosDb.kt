package com.isma3il.photoweatherapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_photos")
data class WeatherPhotosDb(
    @PrimaryKey
    val id:Int,
    @ColumnInfo(name = "photo_path")
    val photoPath:String
)
