package com.isma3il.photoweatherapp.data.network

import com.isma3il.photoweatherapp.data.network.model.WeatherInfo
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {


    @GET("weather")
    fun getWeatherInfo(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double
    ): Observable<WeatherInfo>
}