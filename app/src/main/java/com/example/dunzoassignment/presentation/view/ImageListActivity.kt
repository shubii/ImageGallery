package com.example.dunzoassignment.presentation.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dunzoassignment.R
import com.example.dunzoassignment.databinding.ActivityMainBinding
import com.example.dunzoassignment.network.NetworkCustomError
import com.example.dunzoassignment.network.Status
import com.example.dunzoassignment.presentation.ImageListApp
import com.example.dunzoassignment.presentation.PaginationScrollListener
import com.example.dunzoassignment.presentation.di.ViewModelFactory
import com.example.dunzoassignment.presentation.doAfterTextChanged
import com.example.dunzoassignment.presentation.viewmodels.ImageListViewModel
import javax.inject.Inject


class ImageListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    //ViewModel
    private lateinit var imageListViewModel: ImageListViewModel
    private var imageListAdapter: ImageListAdapter? = null
    private var isLoading = false
    private var isLastPage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.welcome_image_search)

        ImageListApp.applicationComponent.inject(this)
        imageListViewModel = ViewModelProvider(this, viewModelFactory).get(ImageListViewModel::class.java)


        setupAdapter()
        setupTextWatcher()

        imageListViewModel.getImagesLivedata().observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    isLoading = false
                    binding.progressCircular.visibility = View.GONE
                    response.data?.let { photosList ->
                        isLastPage = imageListViewModel.mPageNo >= photosList.totalPages
                        updateList(photosList)
                    }
                }
                Status.ERROR -> {
                    isLoading = false
                    binding.progressCircular.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    handleErrorMsg(response.error!!)
                }
                Status.LOADING -> {
                    isLoading = true
                    binding.progressCircular.visibility = View.VISIBLE
                }
            }
        }


        if (savedInstanceState == null) {
            binding.etSearch.requestFocus()
        } else {
            val position = savedInstanceState.getInt(RECYCLERVIEW_ITEM_POSITION)
            binding.rvImageList.scrollToPosition(position)
        }
    }

    private fun setupAdapter() {
        binding.rvImageList.apply {
            layoutManager = GridLayoutManager(this@ImageListActivity, 3)
            imageListAdapter = ImageListAdapter()
            adapter = imageListAdapter
            addOnScrollListener(OnScrollListener((layoutManager as GridLayoutManager)))
        }
    }

    private fun updateList(photoList : ImageListViewModel.UpdateList) {
        imageListAdapter?.setData(photoList.photoList)
        imageListAdapter?.notifyItemRangeInserted(photoList.startPosition, photoList.count)
    }

    private fun setupTextWatcher() {
        binding.etSearch.doAfterTextChanged { query ->
            binding.tvError.visibility = View.GONE
            imageListViewModel.onQueryUpdate(query.toString().trim())
        }
    }

    inner class OnScrollListener(layoutManager: GridLayoutManager) : PaginationScrollListener(layoutManager) {
        override fun isLoading() = isLoading
        override fun loadMoreItems() = imageListViewModel.loadNextPage()
        override fun isLastPage() = isLastPage
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val lm = binding.rvImageList.layoutManager as GridLayoutManager
        outState.putInt(RECYCLERVIEW_ITEM_POSITION, lm.findFirstCompletelyVisibleItemPosition())
    }

    private fun handleErrorMsg(errorObj: NetworkCustomError) {
        var error = getString(R.string.something_went_wrong)
        when (errorObj.status) {
            701 -> {
                error = "Please connect your device to internet"
            }
            702 -> {
                error = "Some exception occured"
            }
            703 -> {
                error = "Server Error. Please try again"
            }
            704 -> {
                error = "We cannot find any images with keyword ${binding.etSearch.text}"
            }
        }
        binding.tvError.text = error
    }

    companion object {
        private const val RECYCLERVIEW_ITEM_POSITION = "recyclerview item position"
    }
}
