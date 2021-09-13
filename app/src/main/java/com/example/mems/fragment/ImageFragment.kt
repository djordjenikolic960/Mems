package com.example.mems.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mems.R
import com.example.mems.model.ImageViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_gallery_image.image

class ImageFragment : Fragment() {
    private lateinit var imageVM: ImageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initHelpers()
        initView()
    }

    private fun initHelpers() {
        imageVM = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)
    }

    private fun initView() {
        Picasso.get().load(imageVM.selectedImage).into(image)
    }
}