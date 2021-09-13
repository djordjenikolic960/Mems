package com.example.mems.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mems.R
import com.example.mems.adapter.MemsGalleryAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_mems.*


class MemsFragment : Fragment() {
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
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Fetching images....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        // we will get the default FirebaseDatabase instance
        val firebaseDatabase = FirebaseDatabase.getInstance()
        // we will get a DatabaseReference for the database root node
        val databaseReference = firebaseDatabase.reference
        // child node data in the getImage variable
        val images = databaseReference.child(resources.getString(R.string.images_bucket))
        val array = arrayListOf<String>()
        images.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postShow in dataSnapshot.children) {
                    postShow.getValue(String::class.java)?.let { array.add(it) }
                }
                progressDialog.dismiss()
                val adapter = MemsGalleryAdapter(array)
                memsGalleryRecycler.adapter = adapter
                memsGalleryRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "Error Loading Image", Toast.LENGTH_SHORT).show()
            }
        })
    }
}