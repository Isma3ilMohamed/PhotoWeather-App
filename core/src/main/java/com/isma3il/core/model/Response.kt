package com.isma3il.core.model

sealed interface Response<T>{
    data class Success<T>(val data:T):Response<T>
    data class Error<T>(val errorMessage:String):Response<T>
}