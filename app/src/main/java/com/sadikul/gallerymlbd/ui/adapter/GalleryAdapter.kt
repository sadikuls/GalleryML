package com.sadikul.gallerymlbd.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.sadikul.gallerymlbd.R
import com.sadikul.gallerymlbd.data.local.entity.GalleryItemEntity
import com.sadikul.gallerymlbd.data.model.GalleryItem
import com.sadikul.gallerymlbd.databinding.ItemGalleryBinding
import com.sadikul.gallerymlbd.utils.Constants.IMAGE_URL


class GalleryAdapter(var galleryItemList: MutableList<GalleryItemEntity>, val onClick: (GalleryItemEntity?) -> Unit) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    init {
        Log.d("GalleryAdapter","called galleryAdapter")
    }
    inner class GalleryViewHolder(binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val _binding: ItemGalleryBinding = binding

        init {
            itemView.setOnClickListener {
                onClick(galleryItemList[adapterPosition])
            }
        }

        fun bind(galleryItem: GalleryItemEntity) {
            _binding.apply {
                galleryItem.author?.let {
                    authorName.text = it
                }
                val imageUrl = IMAGE_URL + galleryItem.id + "/400/400"
                Log.d("GalleryAdapter",imageUrl)
                Glide.with(ivGallery)
                    .load(IMAGE_URL + galleryItem.id + "/400/400")
                    .apply(
                        RequestOptions().transform(FitCenter()).placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher).priority(Priority.HIGH)
                    )
                    .into(ivGallery)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val _binding =
            ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(galleryItemList[position])
    }

    override fun getItemCount(): Int  = galleryItemList.size

}