package com.example.mems.model

import android.net.Uri

data class GalleryImage(
    val id: Long,
    val displayName: String,
    val contentUri: Uri
)