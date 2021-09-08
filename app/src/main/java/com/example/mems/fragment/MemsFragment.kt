package com.example.mems.fragment

import android.Manifest
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mems.MainActivity
import com.example.mems.MainActivity.Companion.REQUEST_GALLERY_PHOTO
import com.example.mems.R
import com.example.mems.adapter.MemsGalleryAdapter
import com.example.mems.model.GalleryImage
import com.example.mems.util.GridSpacingItemDecoration
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mems.*
import java.io.File


class MemsFragment : Fragment() {
    private lateinit var storageReference: StorageReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mems, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initHelpers()
        initView()
    }

    private fun initHelpers() {
    }

    private fun initView() {
        val localFile = File.createTempFile("tempFile", "jpg")
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Fetching images....")
        progressDialog.setCancelable(false)
        //progressDialog.show()
        var storage = FirebaseStorage.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference.child("Slike")
        storageReference.listAll().let {
            for (image in it.result!!.items) {
                image.downloadUrl.addOnSuccessListener {
                    downloadFile(requireContext(), image.name, ".jpg", requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath, it.toString())
                }
                Picasso.get().load(image.downloadUrl.result).into(ud)
            }
        }

        //todo ovo radi samo storage referece mora da bude ima slike
        //  storageReference = FirebaseStorage.getInstance().reference.child("1630081431243.jpg")
       /* storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            ud.setImageBitmap(bitmap)
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }.addOnFailureListener {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }*/

        /* val galleryAdapter = MemsGalleryAdapter(getImages())
            memsGalleryRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
            memsGalleryRecycler.addItemDecoration(
                GridSpacingItemDecoration(
                    3, spacing = 20, includeEdge = true
                )
            )
            memsGalleryRecycler.adapter = galleryAdapter*/
    }

    private fun downloadFile(context: Context, fileName: String, fileExtension: String, destination: String, url: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(context, destination, fileName + fileExtension)
        downloadManager.enqueue(request)
    }

    private fun getImages(): ArrayList<GalleryImage> {
        var imageList: ArrayList<GalleryImage>
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        activity?.application?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        ).use { cursor ->
            imageList = cursor?.let { it1 -> addImagesFromCursor(it1) }!!
        }
        return imageList
    }

    private fun addImagesFromCursor(cursor: Cursor): ArrayList<GalleryImage> {
        val images = arrayListOf<GalleryImage>()
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val displayName = cursor.getString(displayNameColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            if (displayName != null) {
                val image = GalleryImage(id, displayName, contentUri)
                images += image
            }
        }
        return images
    }
}