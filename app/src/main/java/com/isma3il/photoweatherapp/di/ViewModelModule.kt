package com.isma3il.photoweatherapp.di

import com.isma3il.photoweatherapp.data.repositories.AppRepository
import com.isma3il.photoweatherapp.domain.repositories.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent


@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun bindWeatherRepository(appRepository: AppRepository):Repository
}