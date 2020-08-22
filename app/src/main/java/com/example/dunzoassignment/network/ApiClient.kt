package com.example.dunzoassignment.network
import com.example.dunzoassignment.presentation.models.ImageListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET(GET_IMAGES)
    suspend fun getImagesTextResponseFromServer(@Query("text") text: String, @Query("page") page: String) : ImageListResponse
}