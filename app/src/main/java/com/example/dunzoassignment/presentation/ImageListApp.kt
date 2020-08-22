package com.example.dunzoassignment.presentation

import android.app.Application
import com.example.dunzoassignment.presentation.di.AppComponent
import com.example.dunzoassignment.presentation.di.ApplicationModule
import com.example.dunzoassignment.presentation.di.DaggerAppComponent

class ImageListApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDagger()
    }

    private fun setupDagger() {
        applicationComponent =
                DaggerAppComponent.builder().applicationModule(ApplicationModule(this)).build()
        applicationComponent.inject(this)
    }

    companion object {
        lateinit var applicationComponent: AppComponent
    }
}