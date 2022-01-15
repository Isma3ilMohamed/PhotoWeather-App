package com.isma3il.photoweatherapp.data.network.model

import com.isma3il.core.model.Mapper
import com.isma3il.photoweatherapp.BuildConfig
import com.isma3il.photoweatherapp.domain.model.data.Weather
import javax.inject.Inject

class WeatherMapper @Inject constructor() :Mapper<WeatherInfo, Weather?> {
    override fun map(input: WeatherInfo?): Weather {
        val weather= input?.weather?.get(0)

       return Weather(
           id = input?.id?:0,
           title = weather?.main?:"",
           description = weather?.description?:"",
           icon = "${BuildConfig.IMAGE_URL}${weather?.icon}.png",
           city = input?.name?:"",
           currentTemp = input?.main?.temp?:0.0,
           highTemp = input?.main?.temp_max?:0.0,
           lowTemp = input?.main?.temp_min?:0.0,
           humidity = input?.main?.humidity?:0,
           windSpeed = input?.wind?.speed?:0.0,
           sunrise = input?.sys?.sunrise?.toLong()?:0L,
           sunset = input?.sys?.sunset?.toLong()?:0L,
           currentDate = input?.dt?.toLong()?:0L

       )

    }
}