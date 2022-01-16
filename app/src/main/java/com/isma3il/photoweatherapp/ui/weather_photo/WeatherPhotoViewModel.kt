package com.isma3il.photoweatherapp.ui.weather_photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isma3il.core.model.Response
import com.isma3il.photoweatherapp.domain.model.data.Weather
import com.isma3il.photoweatherapp.domain.model.data.WeatherPhoto
import com.isma3il.photoweatherapp.domain.model.input.LocationInput
import com.isma3il.photoweatherapp.domain.usecases.FetchWeatherInfoUseCase
import com.isma3il.photoweatherapp.domain.usecases.SaveImageInDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class WeatherPhotoViewModel @Inject constructor(
    private val fetchWeatherInfoUseCase: FetchWeatherInfoUseCase,
    private val saveImageInDbUseCase: SaveImageInDbUseCase
) : ViewModel() {

    private val compositeDisposable= CompositeDisposable()

    //progress loading
    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    //error message
    private val _errorMessageLiveData = MutableLiveData<String>()
    val errorMessageLiveData: LiveData<String>
        get() = _errorMessageLiveData

    //weather info
    private val _weatherInfoLiveData = MutableLiveData<Weather>()
    val weatherInfoLiveData: LiveData<Weather>
        get() = _weatherInfoLiveData


    fun getWeatherInfo(lat: Double, lng: Double) {
        compositeDisposable.add(
            fetchWeatherInfoUseCase.execute(LocationInput(lat, lng))
                .doOnSubscribe { _loadingLiveData.postValue(true) }
                .doAfterTerminate { _loadingLiveData.postValue(false) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        when (it) {
                            is Response.Error -> {
                                _errorMessageLiveData.postValue(it.errorMessage)
                            }
                            is Response.Success -> {
                                it.data?.let { _weatherInfoLiveData.postValue(it) }
                            }
                        }
                    },
                    onError = {
                        _errorMessageLiveData.postValue(it.localizedMessage ?: "")
                    }
                )
        )
    }

    //progress loading
    private val _savedImageLiveData = MutableLiveData<Boolean>()
    val savedImageData: LiveData<Boolean>
        get() = _savedImageLiveData

    fun saveImage( photoPath: String) {
       compositeDisposable.add(
           saveImageInDbUseCase.execute(WeatherPhoto(photoPath = photoPath))
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeBy(
                   onNext = {
                       when (it) {
                           is Response.Error -> {
                               _errorMessageLiveData.postValue(it.errorMessage)
                           }
                           is Response.Success -> {
                            it.data?.let { _savedImageLiveData.postValue(it) }
                           }
                       }
                   },
                   onError = {
                       _errorMessageLiveData.postValue(it.localizedMessage ?: "")
                   }
               )
       )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}