package com.isma3il.photoweatherapp.data.di

import android.content.Context
import androidx.room.Room
import com.isma3il.photoweatherapp.data.db.AppCacheHelper
import com.isma3il.photoweatherapp.data.db.CacheHelper
import com.isma3il.photoweatherapp.data.db.WeatherPhotoDatabase
import com.isma3il.photoweatherapp.data.db.WeatherPhotosDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun provideCacheHelper(appCacheHelper: AppCacheHelper):CacheHelper

    companion object{

        @Provides
        fun provideWeatherDatabase(@ApplicationContext context: Context):WeatherPhotoDatabase{
            return Room.databaseBuilder(context,WeatherPhotoDatabase::class.java,"weather_photo.db")
                .build()
        }

        @Provides
        fun provideWeatherDao(database: WeatherPhotoDatabase):WeatherPhotosDao{
            return database.weatherPhotosDao()
        }

    }
}