package com.example.mems.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mems.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_us.*


class UsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_us, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // we will get the default FirebaseDatabase instance
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        // we will get a DatabaseReference for the database root node
        val databaseReference: DatabaseReference = firebaseDatabase.reference

        // Here "image" is the child node value we are getting
        // child node data in the getImage variable
        val getImage: DatabaseReference = databaseReference.child("image")

        // Adding listener for a single change
        // in the data at this location.
        // this listener will triggered once
        // with the value of the data at the location
        getImage.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // getting a DataSnapshot for the location at the specified
                // relative path and getting in the link variable
                val link: String = dataSnapshot.getValue(String::class.java)!!

                // loading that data into rImage
                // variable which is ImageView
                Glide.with(image)
                    .load(link)
                    .centerCrop()
                    .into(image)
               // Picasso.get().load(link).into(image)
            }

            // this will called when any problem
            // occurs in getting data
            override fun onCancelled(databaseError: DatabaseError) {
                // we are showing that error message in toast
                Toast.makeText(requireContext(), "Error Loading Image", Toast.LENGTH_SHORT).show()
            }
        })
    }
}