package com.example.mems.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mems.MainActivity
import com.example.mems.R
import com.example.mems.fragment.ImageFragment
import com.example.mems.model.ImageViewModel
import com.example.mems.util.FragmentHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MemsGalleryAdapter(private val dataSet: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var imageVM: ImageViewModel
    private lateinit var fragmentHelper: FragmentHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.view_gallery_image, parent, false)
        imageVM = ViewModelProvider(parent.context as MainActivity).get(ImageViewModel::class.java)
        fragmentHelper = FragmentHelper(parent.context as MainActivity)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val displayMetrics: DisplayMetrics = holder.itemView.context.resources.displayMetrics
        val widthInPx =
            displayMetrics.widthPixels / 2 - 2 * holder.itemView.context.resources.getDimensionPixelSize(R.dimen.image_margin)
        val params = (holder as GalleryViewHolder).image.layoutParams as GridLayoutManager.LayoutParams
        params.width = widthInPx
        params.height = widthInPx
        holder.image.layoutParams = params
        Picasso.get().load(dataSet[position]).into(holder.image)
        holder.itemView.setOnClickListener {
            imageVM.selectedImage = dataSet[position]
            fragmentHelper.replaceFragment(ImageFragment::class.java)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
    }
}