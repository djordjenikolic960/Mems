package com.example.mems.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mems.R
import com.example.mems.model.GalleryImage

class MemsGalleryAdapter(private val dataSet: ArrayList<GalleryImage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.view_gallery_image, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val displayMetrics: DisplayMetrics = holder.itemView.context.resources.displayMetrics
        val widthInPx =
            displayMetrics.widthPixels / 3
        val params = (holder as GalleryViewHolder).image.layoutParams as FrameLayout.LayoutParams
        params.width = widthInPx
        params.height = widthInPx
        holder.image.layoutParams = params
        val image = dataSet[position]
        Glide.with(holder.image)
            .load(image.contentUri)
            .centerCrop()
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        var checkCircle: ImageView = view.findViewById(R.id.checkCircle)
        var check: ImageView = view.findViewById(R.id.check)
    }
}