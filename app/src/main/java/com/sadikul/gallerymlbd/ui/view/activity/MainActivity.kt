package com.sadikul.gallerymlbd.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mindorks.framework.mvvm.ui.main.viewmodel.GalleryViewModel
import com.sadikul.gallerymlbd.R
import com.sadikul.gallerymlbd.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupObserver()
    }

    private fun setupObserver() {
        Log.d(TAG, " gallery-app setupObserver")
        viewModel.images.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d(TAG, "networking got the data ${it.data?.size}")
                }
                Status.LOADING -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "networking loading started")
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "networking got error")
                }
            }
        })
    }
}