package com.isma3il.photoweatherapp.domain.usecases

import com.isma3il.core.base.BaseUseCase
import com.isma3il.core.model.Response
import com.isma3il.photoweatherapp.domain.model.data.WeatherPhoto
import com.isma3il.photoweatherapp.domain.repositories.Repository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.Observer
import javax.inject.Inject

class SaveImageInDbUseCase @Inject constructor(
    private val repository: Repository
) :
    BaseUseCase<WeatherPhoto, Observable<Response<Boolean>>>() {
    override fun execute(input: WeatherPhoto?): Observable<Response<Boolean>> {
       return if (input != null) {
           val result= repository.addPhotoToHistory(input)
           result.andThen(ObservableSource<Response<Boolean>> {
               it.onNext(Response.Success(true))
           })
        }else{
            Observable.just(Response.Error(""))
        }
    }
}