package com.isma3il.photoweatherapp.data.network.interceptors

import com.isma3il.photoweatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiInterceptor @Inject constructor():Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original=chain.request()
        val originalHttpUrl=original.url
        val url=originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY,BuildConfig.API_KEY)
            .build()

        val requestBuilder=original.newBuilder().url(url)
        return chain.proceed(requestBuilder.build())
    }

    companion object{
        private const val API_KEY="appid"
    }
}