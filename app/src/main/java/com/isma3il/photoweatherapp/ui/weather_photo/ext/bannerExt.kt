package com.isma3il.photoweatherapp.ui.weather_photo.ext

import android.annotation.SuppressLint
import com.isma3il.core.utils.loadImage
import com.isma3il.photoweatherapp.domain.model.data.Weather
import com.isma3il.photoweatherapp.ui.weather_photo.WeatherPhotoFragment
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


@SuppressLint("SetTextI18n", "SimpleDateFormat")
fun WeatherPhotoFragment.bindWeather(weather: Weather)= with(binding.layoutWeather){

    tvCity.text=weather.city
    tvDate.text=SimpleDateFormat("EEE,MMMM dd - H:mm a").format(Date())
    ivTempIcon.loadImage(weather.icon)

    tvCurrentTemp.text="${weather.currentTemp.toInt()}°"
    tvMinMaxTemp.text="${weather.highTemp.toInt()}° / ${weather.lowTemp.toInt()}°"
    tvDesc.text=weather.description

    tvSunrise.text=SimpleDateFormat("h:mm a").format(Date(weather.sunrise))
    tvSunset.text=SimpleDateFormat("h:mm a").format(Date(weather.sunset))

    tvWindSpeed.text="${weather.windSpeed}"
    tvHumidity.text="${weather.humidity}%"

}