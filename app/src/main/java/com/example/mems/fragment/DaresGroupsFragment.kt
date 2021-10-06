package com.example.mems.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mems.R
import com.example.mems.model.DareListItem
import com.example.mems.model.DaresPerson
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_dares_list.*

class DaresGroupsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dares_list, container, false)
        val databaseReference = FirebaseDatabase.getInstance().reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dares = arrayListOf<DareListItem>()
                for (postSnapshot in dataSnapshot.children) {
                    postSnapshot.getValue(DareListItem::class.java)?.let { dares.add(it) }
                }
                initView(dares)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        databaseReference.child(resources.getString(R.string.firebase_dares_list)).addValueEventListener(postListener)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
      /*  for (i in 0..DaresCategories.values().size) {
            val generatedId: String = databaseReference.push().key!!
            val dare = DareListItem(generatedId, DaresCategories.values()[i].name, "naslov", "neki tekst", 0, "djole")
            databaseReference.child(getString(R.string.firebase_dares_list)).child(generatedId).setValue(dare)
        }*/
    }

    private fun initView(dares: ArrayList<DareListItem>) {
        val groups = ArrayList<ArrayList<DareListItem>>(DaresCategories.values().size)
        for(index in 0 until dares.size) {
            when (dares[index].category) {
                DaresCategories.ROMANTIC.name -> groups[DaresCategories.ROMANTIC.ordinal].add(dares[index])
                DaresCategories.ADVENTURES.name -> groups[DaresCategories.ADVENTURES.ordinal].add(dares[index])
                DaresCategories.IMPROVE_COMMUNICATION.name -> groups[DaresCategories.IMPROVE_COMMUNICATION.ordinal].add(dares[index])
                DaresCategories.QUESTIONS.name -> groups[DaresCategories.QUESTIONS.ordinal].add(dares[index])
                DaresCategories.OUTDOOR.name -> groups[DaresCategories.OUTDOOR.ordinal].add(dares[index])
                DaresCategories.AT_HOME.name -> groups[DaresCategories.AT_HOME.ordinal].add(dares[index])
                DaresCategories.THEMATIC.name -> groups[DaresCategories.THEMATIC.ordinal].add(dares[index])
                DaresCategories.SEASON.name -> groups[DaresCategories.SEASON.ordinal].add(dares[index])
                DaresCategories.AT_THE_KITCHEN.name -> groups[DaresCategories.AT_THE_KITCHEN.ordinal].add(dares[index])
                DaresCategories.DIY.name -> groups[DaresCategories.DIY.ordinal].add(dares[index])
                DaresCategories.CUSTOM.name -> groups[DaresCategories.CUSTOM.ordinal].add(dares[index])
            }
        }
        val array = groups
      /*  for (dare in dares) {
            val view = layoutInflater.inflate(R.layout.view_dares_group_card, daresLayout, false)
            daresLayout.addView(view)
        }*/
    }

    enum class DaresCategories {
        ROMANTIC,
        ADVENTURES,
        IMPROVE_COMMUNICATION,
        QUESTIONS,
        OUTDOOR,
        AT_HOME,
        THEMATIC,
        SEASON,
        AT_THE_KITCHEN,
        DIY,
        CUSTOM
    }
}