package com.example.dunzoassignment.presentation.di


import com.example.dunzoassignment.presentation.ImageListApp
import com.example.dunzoassignment.presentation.view.ImageListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(imageListApp: ImageListApp)
    fun inject(imageListActivity: ImageListActivity)
}