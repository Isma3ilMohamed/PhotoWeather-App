package com.isma3il.photoweatherapp.data.db.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.isma3il.photoweatherapp.domain.model.WeatherPhoto

@Entity(tableName = "weather_photos")
data class WeatherPhotosEntity(
    @PrimaryKey
    val id:Int,
    @ColumnInfo(name = "photo_path")
    val photoPath:String
){


    companion object{
        fun fromDomain(photo:WeatherPhoto):WeatherPhotosEntity{
            return WeatherPhotosEntity(photo.id,photo.photoPath)
        }

        fun toDomain(photo:WeatherPhotosEntity):WeatherPhoto{
            return WeatherPhoto(photo.id,photo.photoPath)
        }
    }
}
