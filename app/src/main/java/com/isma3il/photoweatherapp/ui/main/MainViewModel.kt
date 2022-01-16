package com.isma3il.photoweatherapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isma3il.core.model.Response
import com.isma3il.photoweatherapp.domain.model.data.WeatherPhoto
import com.isma3il.photoweatherapp.domain.usecases.FetchHistoryPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val historyPhotosUseCase: FetchHistoryPhotosUseCase
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


    //observe history photos
    private val _historyLiveData = MutableLiveData<List<WeatherPhoto>>()
    val historyLiveData: LiveData<List<WeatherPhoto>>
        get() = _historyLiveData

    fun getHistory() {
        _loadingLiveData.postValue(true)
      compositeDisposable.add(
          historyPhotosUseCase.execute(null)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeBy(
                  onNext = {
                      when (it) {
                          is Response.Error -> { _errorMessageLiveData.postValue(it.errorMessage)}
                          is Response.Success -> {
                              _loadingLiveData.postValue(false)
                              it.data?.let { _historyLiveData.postValue(it) }
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