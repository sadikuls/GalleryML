package com.pactice.hild_mvvm_room.ui.main.view.fragment

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
import com.mindorks.framework.mvvm.ui.main.viewmodel.GalleryViewModel
import com.sadikul.gallerymlbd.R
import com.sadikul.gallerymlbd.data.model.GalleryItem
import com.sadikul.gallerymlbd.databinding.FragmentGalleryBinding
import com.sadikul.gallerymlbd.ui.adapter.GalleryAdapter
import com.sadikul.gallerymlbd.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gallery.*

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val galleryViewModel : GalleryViewModel by viewModels()
    private lateinit var _binding : FragmentGalleryBinding
    private val galleryList: MutableList<GalleryItem> by lazy { ArrayList<GalleryItem>() }
    private val TAG = GalleryFragment::class.java.simpleName
    private lateinit var galleryAdapter: GalleryAdapter
    private val imgIdArgs = "imgId"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)
        setupRecyclerView()
        setupObserver()
    }

    private fun setupObserver() {
        Log.d(TAG," gallery-app setupObserver")
        galleryViewModel.images.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    hideLoader(true)
                    it.data?.let {
                            images -> updateList(images)
                    }
                }

                Status.LOADING -> {
                    showLoader()
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG,"gallery-app loading started")
                }

                Status.ERROR -> {
                    hideLoader(false)
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "gallery-app got error")
                }
            }
        })
    }

    private fun showLoader() = _binding?.apply {
        progrssBar.visibility = View.VISIBLE
        galleryRecyclerview.visibility = View.GONE
    }

    private fun hideLoader(showRecyclerview: Boolean) = _binding?.apply {
        progrssBar.visibility = View.GONE
        galleryRecyclerview.visibility = if (showRecyclerview) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        _binding.galleryRecyclerview.apply {
            galleryAdapter = GalleryAdapter(galleryList)
            layoutManager = GridLayoutManager(context, 2)
            adapter = galleryAdapter
        }
    }

    private fun updateList(images: List<GalleryItem>) {
        images?.let {
            galleryList.apply {
                clear()
                addAll(it)
                galleryAdapter.notifyDataSetChanged()
                Log.d("updateData", "Yes ${images.size}")
            }
        }
    }
}