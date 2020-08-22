package com.example.dunzoassignment.data.repository


import android.content.Context
import android.util.Log
import com.example.dunzoassignment.BuildConfig
import com.example.dunzoassignment.domain.repo.ImageListDataSource
import com.example.dunzoassignment.network.ApiClient
import com.example.dunzoassignment.network.NetworkResult
import com.example.dunzoassignment.presentation.isNetworkAvailable
import com.example.dunzoassignment.presentation.models.ImageListResponse
import com.google.gson.Gson
import javax.inject.Inject


class ImageListRemoteDataSource @Inject constructor(
    private val apiClient: ApiClient, private val context: Context) :
    ImageListDataSource {

    override suspend fun getImageList(text:String, page:String): NetworkResult<ImageListResponse> {
        if (!isNetworkAvailable(context)) return NetworkResult.NetworkError()

        var result: NetworkResult<ImageListResponse>? = null
        runCatching {
            val response = apiClient.getImagesTextResponseFromServer(text, page)
            result = NetworkResult.Success(response)
        }.onFailure {
            result = NetworkResult.Error(it)
        }
        return result!!
    }
}