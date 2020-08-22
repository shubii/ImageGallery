package com.example.dunzoassignment.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.dunzoassignment.domain.repo.ImageListDataSource
import com.example.dunzoassignment.network.NetworkCustomError
import com.example.dunzoassignment.network.NetworkResult
import com.example.dunzoassignment.network.Resource
import com.example.dunzoassignment.presentation.isNetworkAvailable
import com.example.dunzoassignment.presentation.models.Photo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class ImageListViewModel @Inject constructor(@Named("images_remote_data") private val imagesRepo: ImageListDataSource, private val app: Application) :
    AndroidViewModel(app) {

    var mPageNo: Int = 0
    private var totalPhotosList: ArrayList<Photo> = ArrayList()
    private var job: Job? = null
    private val queryLiveData = MutableLiveData<String>()
    private val imagesList = MediatorLiveData<Resource<UpdateList>>().apply {
        addSource(queryLiveData) { query ->
            if (query.isNotEmpty()) {
                totalPhotosList.clear()
                mPageNo = 1
                fetchImages(query, mPageNo)
            }
        }
    }

    fun getImagesLivedata(): LiveData<Resource<UpdateList>> {
        return imagesList
    }

    private fun fetchImages(text: String, pageNo: Int) {
        imagesList.value = Resource.loading()
        viewModelScope.launch {
            val response = imagesRepo.getImageList(text, pageNo.toString())

            when (response) {
                is NetworkResult.Success -> {
                    val imageListResponse = response.data
                    if ("ok".equals(response.data.status, true)) {
                        val photosModel = imageListResponse.photos
                        if (photosModel.photo.isNotEmpty() && photosModel.photo.size > 0) {
                            val size = totalPhotosList.size
                            totalPhotosList.addAll(photosModel.photo)
                            imagesList.value = Resource.success(UpdateList(totalPhotosList, size, photosModel.photo.size, photosModel.pages))
                        } else {
                            imagesList.value = Resource.error(NetworkCustomError(status = 704))
                        }
                    } else {
                        imagesList.value = Resource.error(NetworkCustomError(status = 703))
                    }
                }

                is NetworkResult.Error -> {
                    if (response.exception !is CancellationException) {
                        imagesList.value = Resource.error(NetworkCustomError(status = 702))
                    }
                }

                is NetworkResult.NetworkError -> {
                    imagesList.value = Resource.error(NetworkCustomError(status = 701))
                }
            }
        }
    }

    fun loadNextPage() {
        if (!isNetworkAvailable(app)) return
        mPageNo++
        fetchImages(queryLiveData.value.toString(), mPageNo)
    }

    fun onQueryUpdate(query: String) {
        job?.cancel()
        if (query.equals(queryLiveData.value.toString(),true) || query.isEmpty()) return

        job = viewModelScope.launch {
            delay(800)
            queryLiveData.value = query.trim()
        }

    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

    data class UpdateList(val photoList : ArrayList<Photo>, val startPosition: Int, val count : Int, val totalPages: Int)

}