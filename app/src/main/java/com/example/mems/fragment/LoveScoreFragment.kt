package com.example.mems.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mems.R
import com.example.mems.model.Person
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_love_score.view.*

class LoveScoreFragment : Fragment() {

    companion object {
        const val ECI_INDEX = 0
        const val MALBI_INDEX = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_love_score, container, false)
        val databaseReference = FirebaseDatabase.getInstance().reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val people = arrayListOf<Person>()
                for (postSnapshot in dataSnapshot.children) {
                    postSnapshot.getValue(Person::class.java)?.let { people.add(it) }
                }
                view.eciScore.text = people[ECI_INDEX].score.toString()
                view.malbiScore.text = people[MALBI_INDEX].score.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        databaseReference.child(resources.getString(R.string.firebase_love_score_list)).addValueEventListener(postListener)
        view.btnAddBeci.setOnClickListener { updateScore(isEci = true, isIncrement = true) }
        view.btnSubBeci.setOnClickListener { updateScore(isEci = true, isIncrement = false) }
        view.btnAddMalbi.setOnClickListener { updateScore(isEci = false, isIncrement = true) }
        view.btnSubMalbi.setOnClickListener { updateScore(isEci = false, isIncrement = false) }
        return view
    }

    private fun updateScore(isEci: Boolean, isIncrement: Boolean) {
        val databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child(resources.getString(R.string.firebase_love_score_list)).child(
            getFirebaseChild(isEci)
        ).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val person = dataSnapshot.getValue(Person::class.java)!!
                    person.score = if (isIncrement) {
                        person.score!! + 1
                    } else {
                        person.score!! - 1
                    }
                    databaseReference.child(resources.getString(R.string.firebase_love_score_list)).child(getFirebaseChild(isEci)).setValue(person)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getFirebaseChild(isEci: Boolean): String {
        return if (isEci) {
            resources.getString(R.string.name_eci)
        } else {
            resources.getString(R.string.name_malbi)
        }
    }
}