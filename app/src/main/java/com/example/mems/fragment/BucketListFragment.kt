package com.example.mems.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mems.MainActivity
import com.example.mems.R
import com.example.mems.adapter.BucketListAdapter
import com.example.mems.dialog.AddBucketListItemDialog
import com.example.mems.model.BucketListItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.fragment_bucket_list.*
import kotlinx.android.synthetic.main.fragment_bucket_list.view.*

class BucketListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bucket_list, container, false)
        view.bucketListRecycler.layoutManager = LinearLayoutManager(requireContext())
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val bucketList = arrayListOf<BucketListItem>()
                for (postSnapshot in dataSnapshot.children) {
                    val bucketListItem = postSnapshot.getValue(BucketListItem::class.java)
                    bucketList.add(bucketListItem!!)
                }
                val adapter = BucketListAdapter(bucketList)
                view.bucketListRecycler.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        databaseReference.child(resources.getString(R.string.firebase_bucket_list)).addValueEventListener(postListener)
        (activity as MainActivity).findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            AddBucketListItemDialog().show(requireActivity().supportFragmentManager, "")
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}