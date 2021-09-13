package com.example.mems.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    var selectedImage: String? = null
}