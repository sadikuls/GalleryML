package com.sadikul.gallerymlbd.ui.view.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.sadikul.gallerymlbd.R
import com.sadikul.gallerymlbd.databinding.FragmentImageShowBinding
import com.sadikul.gallerymlbd.utils.Constants.IMAGE_URL

class ImageShowFragment : Fragment(R.layout.fragment_image_show) {
    private val TAG = ImageShowFragment::class.java.simpleName
    private val args: ImageShowFragmentArgs by navArgs()

    private lateinit var _binding: FragmentImageShowBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentImageShow","onViewCreated previewFragment")
        _binding = FragmentImageShowBinding.bind(view)
        loadImage()
    }

    private fun loadImage() = _binding?.apply {
        val options: RequestOptions = RequestOptions()
            .transform(FitCenter())
            .priority(Priority.HIGH)

        Glide.with(ivPhoto.context)
            .load(IMAGE_URL + args.imgId + "/800/800")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    ivLoadProgrssBar.visibility = View.GONE
                    return false
                }

            })
            .apply(options)
            .into(ivPhoto)
    }

}