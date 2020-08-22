package com.example.dunzoassignment.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dunzoassignment.data.repository.ImageListRemoteDataSource
import com.example.dunzoassignment.domain.repo.ImageListDataSource
import com.example.dunzoassignment.presentation.viewmodels.ImageListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Singleton

//Module that provides viewModel factory and viewmodels. Please add entry here when creating new viewmodel so view models can be injected.


@Module
abstract class ViewModelModule {


    @Binds
    @Singleton
    @Named("images_remote_data")
    abstract fun provideImagesRemoteDataSource(imagesRemoteDataSource: ImageListRemoteDataSource): ImageListDataSource


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ImageListViewModel::class)
    internal abstract fun provideImagesViewModel(imagesViewModel: ImageListViewModel): ViewModel


}