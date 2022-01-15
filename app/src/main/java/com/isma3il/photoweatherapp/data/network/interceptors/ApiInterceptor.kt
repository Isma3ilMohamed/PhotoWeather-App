package com.isma3il.photoweatherapp.data.network.interceptors

import com.isma3il.photoweatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiInterceptor @Inject constructor():Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original=chain.request()

        val url=original.url.newBuilder()
            .addQueryParameter(API_KEY,BuildConfig.API_KEY)
            .addQueryParameter(UNITS,"metric")
            .build()

        val requestBuilder=original.newBuilder().url(url)
        return chain.proceed(requestBuilder.build())
    }

    companion object{
        private const val API_KEY="appid"
        private const val UNITS="units"
    }
}