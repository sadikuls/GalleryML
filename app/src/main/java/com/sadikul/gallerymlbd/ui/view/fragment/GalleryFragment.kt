package com.sadikul.gallerymlbd.ui.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindorks.framework.mvvm.ui.main.viewmodel.GalleryViewModel
import com.sadikul.gallerymlbd.R
import com.sadikul.gallerymlbd.data.local.entity.GalleryItemEntity
import com.sadikul.gallerymlbd.databinding.FragmentGalleryBinding
import com.sadikul.gallerymlbd.ui.adapter.GalleryAdapter
import com.sadikul.gallerymlbd.utils.NetworkHelper
import com.sadikul.gallerymlbd.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val galleryViewModel : GalleryViewModel by viewModels()
    private lateinit var _binding : FragmentGalleryBinding
    private val galleryList: MutableList<GalleryItemEntity> by lazy { ArrayList<GalleryItemEntity>() }
    private val TAG = GalleryFragment::class.java.simpleName
    private lateinit var galleryAdapter: GalleryAdapter
    private val imgIdArgs = "imgId"
    private val imgDownloadLinkArgs = "downloadLink"
    private val imageNameArgs = "imageName"
    private var isLoading = false
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisibleItems = 0
    private var previousTotal = 0
    private var viewThreshold = 30
    private var pageNumber = 0

    @Inject lateinit var networkHelper: NetworkHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)
        setupRecyclerView()
        setupObserver()
        if(galleryList.size == 0) galleryViewModel.fetchImages(pageNumber = pageNumber, limit = viewThreshold)
    }

    private fun setupObserver() {
        Log.d(TAG, " gallery-app setupObserver")
        galleryViewModel.images.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoader(true)
                    it.data?.let { images ->
                        updateList(images)
                    }
                }

                Status.LOADING -> {
                    showLoader()
                    //Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "gallery-app loading started")
                }

                Status.ERROR -> {
                    hideLoader(false)
                    //Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "gallery-app got error")
                }
            }
        })
    }

    private fun showLoader() = _binding?.apply {
        progrssBar.visibility = View.VISIBLE
        //galleryRecyclerview.visibility = View.GONE
    }

    private fun hideLoader(showRecyclerview: Boolean) = _binding?.apply {
        progrssBar.visibility = View.GONE
        //galleryRecyclerview.visibility = if (showRecyclerview) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context, 3)
        _binding.galleryRecyclerview.apply {
            galleryAdapter = GalleryAdapter(galleryList){ item ->
                navigateToImagePreview(item)
            }
            layoutManager = gridLayoutManager
            adapter = galleryAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    visibleItemCount = gridLayoutManager.childCount
                    totalItemCount = gridLayoutManager.itemCount
                    pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition()
                    if (dy > 0) {
                        if(!networkHelper.isNetworkConnected()){
                            Toast.makeText(context, "Network not available..", Toast.LENGTH_LONG).show()
                            return
                        }
                        if (isLoading) {
                            if (totalItemCount > previousTotal) {
                                isLoading = false
                                previousTotal = totalItemCount
                            }
                        }
                        if (!isLoading && ((totalItemCount - visibleItemCount) <= pastVisibleItems + viewThreshold)) {
                            // fetch data
                            isLoading = true
                            Log.d(
                                TAG,
                                "pagination fetch-data pageNumber $pageNumber viewThreshold $viewThreshold"
                            )
                            galleryViewModel.fetchImages(
                                pageNumber = pageNumber,
                                limit = viewThreshold
                            )
                            pageNumber++
                        }
                    }
                }
            })
        }

    }

    private fun updateList(images: List<GalleryItemEntity>) {
        images?.let {
            galleryList.apply {
                //clear()
                addAll(it)
                galleryAdapter.notifyDataSetChanged()
                Log.d("updateData", "Yes ${images.size}")
            }
            _binding.tvNumberOfPhotos?.text = "${galleryList.size} Photos"
        }
    }

    private fun navigateToImagePreview(item: GalleryItemEntity?) {
        NavHostFragment.findNavController(this@GalleryFragment).navigate(
            R.id.action_galleryFragment_to_imageShowFragment,
            bundleOf(
                imgIdArgs to item?.id,
                imgDownloadLinkArgs to item?.download_url,
                imageNameArgs to item?.author
            )
        )
    }
}