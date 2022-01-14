package com.isma3il.core.model

sealed interface ApiResponse<T>{
    data class Success<T>(val data:T):ApiResponse<T>
    data class Error<T>(val errorMessage:String):ApiResponse<T>
}