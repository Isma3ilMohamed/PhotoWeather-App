package com.isma3il.photoweatherapp.domain.usecases

import com.isma3il.core.base.BaseUseCase
import com.isma3il.core.model.Response
import com.isma3il.photoweatherapp.domain.model.data.WeatherPhoto
import com.isma3il.photoweatherapp.domain.repositories.Repository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class FetchHistoryPhotosUseCase @Inject constructor(
    private val repository: Repository
) : BaseUseCase<Nothing, Observable<Response<List<WeatherPhoto>>>>() {
    override fun execute(input: Nothing?): Observable<Response<List<WeatherPhoto>>> {
        return repository.fetchHistoryPhotos().map {
            Response.Success(it)
        }
    }


}