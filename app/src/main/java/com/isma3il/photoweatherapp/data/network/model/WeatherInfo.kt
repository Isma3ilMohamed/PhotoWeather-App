package com.isma3il.photoweatherapp.data.network.model

data class WeatherInfo(
    var base: String,
    var clouds: Clouds,
    var cod: Int,
    var coord: Coord,
    var dt: Int,
    var id: Int,
    var main: Main,
    var name: String,
    var sys: Sys,
    var timezone: Int,
    var visibility: Int,
    var weather: List<Weather>,
    var wind: Wind
) {
    data class Clouds(
        var all: Int
    )

    data class Coord(
        var lat: Double,
        var lon: Double
    )

    data class Main(
        var feels_like: Double,
        var grnd_level: Int,
        var humidity: Int,
        var pressure: Int,
        var sea_level: Int,
        var temp: Double,
        var temp_max: Double,
        var temp_min: Double
    )

    data class Sys(
        var country: String,
        var id: Int,
        var sunrise: Int,
        var sunset: Int,
        var type: Int
    )

    data class Weather(
        var description: String,
        var icon: String,
        var id: Int,
        var main: String
    )

    data class Wind(
        var deg: Int,
        var gust: Double,
        var speed: Double
    )
}