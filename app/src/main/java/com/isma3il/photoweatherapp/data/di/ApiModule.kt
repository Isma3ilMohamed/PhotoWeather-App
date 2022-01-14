package com.isma3il.photoweatherapp.data.di

import com.isma3il.photoweatherapp.BuildConfig
import com.isma3il.photoweatherapp.data.network.Api
import com.isma3il.photoweatherapp.data.network.AppNetworkHelper
import com.isma3il.photoweatherapp.data.network.NetworkHelper
import com.isma3il.photoweatherapp.data.network.interceptors.ApiInterceptor
import com.isma3il.photoweatherapp.data.network.interceptors.LoggingInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class ApiModule {


    @Binds
    abstract fun bindNetworkHelper(networkHelper: AppNetworkHelper):NetworkHelper

    companion object {
        @Provides
        @Singleton
        fun provideWeatherApi(okHttpClient: OkHttpClient): Api {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(Api::class.java)
        }

        @Provides
        fun provideOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            apiInterceptor: ApiInterceptor
        ): OkHttpClient {

            return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(apiInterceptor)
                .build()
        }

        @Provides
        fun provideHttpLoggingInterceptor(loggingInterceptor: LoggingInterceptor): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor(loggingInterceptor)

            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return interceptor
        }
    }
}