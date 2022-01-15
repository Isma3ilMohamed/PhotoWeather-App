package com.isma3il.photoweatherapp.domain.usecases

import com.isma3il.core.base.BaseUseCase
import com.isma3il.core.model.Response
import com.isma3il.photoweatherapp.data.network.model.WeatherInfo
import com.isma3il.photoweatherapp.domain.model.data.Weather
import com.isma3il.photoweatherapp.domain.model.input.LocationInput
import com.isma3il.photoweatherapp.domain.repositories.Repository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FetchWeatherInfoUseCase @Inject constructor(
    private val repository: Repository
) :
    BaseUseCase<LocationInput, Observable<Response<Weather>>>() {
    override fun execute(input: LocationInput?): Observable<Response<Weather>> {

        return repository.fetchWeatherInfo(input?.lat ?: 0.0, input?.lng ?: 0.0).map {
            if (it != null) {
                Response.Success(it)
            } else {
                Response.Error("No result")
            }
        }
    }
}