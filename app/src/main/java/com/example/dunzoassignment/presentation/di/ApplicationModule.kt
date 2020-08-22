package com.example.dunzoassignment.presentation.di

import android.app.Application
import android.content.Context
import com.example.dunzoassignment.presentation.ImageListApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private var imageListApp: ImageListApp) {

    @Provides
    @Singleton
    fun provideApp(): Application {
        return imageListApp
    }

    @Provides
    @Singleton
    fun getContext(): Context = imageListApp
}