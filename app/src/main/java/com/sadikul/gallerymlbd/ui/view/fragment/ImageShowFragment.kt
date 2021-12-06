package com.sadikul.gallerymlbd.ui.view.fragment

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kishan.askpermission.AskPermission
import com.kishan.askpermission.PermissionCallback
import com.sadikul.gallerymlbd.R
import com.sadikul.gallerymlbd.databinding.FragmentImageShowBinding
import kotlinx.android.synthetic.main.fragment_image_show.*


class ImageShowFragment : Fragment(R.layout.fragment_image_show), PermissionCallback{
    private val TAG = ImageShowFragment::class.java.simpleName
    private val args: ImageShowFragmentArgs by navArgs()

    private lateinit var _binding: FragmentImageShowBinding
    private val PERMISSION_REQUEST_CODE = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentImageShow", "onViewCreated previewFragment")
        _binding = FragmentImageShowBinding.bind(view)
        loadImage()
        handleClickAction()
        hideLayoutWithDelay()
    }

    private fun handleClickAction() {
        _binding.apply {
            tvImageName?.text = args.imageName
            btnDownload?.setOnClickListener {
                if (checkRuntimePermission()) {
                    downloadUsingAndroidDownloadManager()
                }
            }

            btnShare?.setOnClickListener {
                shareImage()
            }

            buttonBack?.setOnClickListener {
                NavHostFragment.findNavController(this@ImageShowFragment).popBackStack()
            }
            ivPhoto.setOnClickListener({
                _binding.apply {
                    layoutBottom?.visibility = View.VISIBLE
                    buttonBack?.visibility = View.VISIBLE
                }
                hideLayoutWithDelay()
            })
        }
    }

    fun hideLayoutWithDelay(){
        Handler().postDelayed({
            _binding.apply {
                layoutBottom?.visibility = View.GONE
                buttonBack?.visibility = View.GONE
                //activity?.getWindow()?.getDecorView()?.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN)
            }

        }, 5 * 1000)
    }

    fun checkRuntimePermission() : Boolean{
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            AskPermission.Builder(this).setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .setCallback(this)
                .request(PERMISSION_REQUEST_CODE)
            return false
        }
        return true
    }

    private fun loadImage() = _binding?.apply {
        val options: RequestOptions = RequestOptions()
            .transform(FitCenter())
            .priority(Priority.HIGH)

        Glide.with(ivPhoto.context)
            .load(args.downloadLink)
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


    override fun onPermissionsGranted(requestCode: Int) {
        downloadUsingAndroidDownloadManager()
        Log.d(TAG, "Download file-download onPermissionsGranted $requestCode")
    }

    override fun onPermissionsDenied(requestCode: Int) {

        Log.d(TAG, "Download file-download onPermissionsDenied $requestCode")
    }

    fun downloadUsingAndroidDownloadManager(){
        Toast.makeText(context, "Downloading...",Toast.LENGTH_LONG).show()
        Log.d(
            TAG,
            "downloadUsingAndroidDownloadManager file-download url ${args.downloadLink} name ${args.imageName}"
        )
        val imageName = args.imageName+".jpeg"
        val dmrequest = DownloadManager.Request(Uri.parse(args.downloadLink))
            .setTitle(imageName)
            .setDescription("Downloading " + imageName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, imageName)
        val manager =  requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(dmrequest)
    }

    fun shareImage(){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, args.downloadLink)
        startActivity(Intent.createChooser(shareIntent, "Share image using"))
    }
}