package com.isma3il.photoweatherapp.domain.model

data class Weather(
    val id:Int,
    val title: String,
    val description: String,
    val icon:String,
    val city:String,
    val currentTemp:Double,
    val highTemp:Double,
    val lowTemp:Double,
    val pressure:Int,
    val humidity:Int,
    val windSpeed:Double,
    val sunrise:Int,
    val sunset:Int
)
