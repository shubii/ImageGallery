package com.example.dunzoassignment.domain.repo

import com.example.dunzoassignment.network.NetworkResult
import com.example.dunzoassignment.presentation.models.ImageListResponse


interface ImageListDataSource {

    suspend fun getImageList(text: String, page:String): NetworkResult<ImageListResponse>
}