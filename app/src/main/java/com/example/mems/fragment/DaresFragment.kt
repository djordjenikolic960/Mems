package com.example.mems.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.mems.MainActivity
import com.example.mems.R
import com.example.mems.adapter.DaresAdapter
import com.example.mems.model.DaresPerson
import com.example.mems.model.Person
import com.example.mems.util.FragmentHelper
import com.example.mems.util.PreferencesHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.dares_score_view.*
import kotlinx.android.synthetic.main.dares_score_view.view.*
import kotlinx.android.synthetic.main.fragment_dares.*
import kotlinx.android.synthetic.main.fragment_love_score.view.*
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class DaresFragment : Fragment() {
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var fragmentHelper: FragmentHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dares, container, false)
        val databaseReference = FirebaseDatabase.getInstance().reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val people = arrayListOf<DaresPerson>()
                for (postSnapshot in dataSnapshot.children) {
                    postSnapshot.getValue(DaresPerson::class.java)?.let { people.add(it) }
                }
                view.eciDaresScore.text = people[LoveScoreFragment.ECI_INDEX].score.toString()
                view.malbiDaresScore.text = people[LoveScoreFragment.MALBI_INDEX].score.toString()
                view.eciProgressBar.max = getProgressMaxValue(people[LoveScoreFragment.ECI_INDEX].score!!)
                view.eciProgressBar.progress = people[LoveScoreFragment.ECI_INDEX].score!!
                view.eciProgressText.text = StringBuilder().append(people[LoveScoreFragment.ECI_INDEX].score!!).append("/").append(getProgressMaxValue(people[LoveScoreFragment.ECI_INDEX].score!!))
                view.malbiProgressBar.max = getProgressMaxValue(people[LoveScoreFragment.MALBI_INDEX].score!!)
                view.malbiProgressBar.progress =  people[LoveScoreFragment.MALBI_INDEX].score!!
                view.malbiProgressText.text = StringBuilder().append(people[LoveScoreFragment.MALBI_INDEX].score!!).append("/").append(getProgressMaxValue(people[LoveScoreFragment.MALBI_INDEX].score!!))
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        databaseReference.child(resources.getString(R.string.firebase_dares_person_list)).addValueEventListener(postListener)
        (activity as MainActivity).findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            fragmentHelper.replaceFragment(DaresGroupsFragment::class.java)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initHelpers()
        initView()
        initListeners()
    }

    private fun initHelpers() {
        fragmentHelper = FragmentHelper(requireActivity())
    }

    private fun initView() {
        daresViewPager.adapter = DaresAdapter(requireContext())
    }

    private fun initListeners() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    daresViewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    daresViewPager.currentItem = tab.position
                }
            }
        })
    }

    private fun getProgressMaxValue(score: Int): Int {
        return when (score) {
            in 0..49 -> 50
            in 50..99 -> 100
            in 100..249 -> 250
            in 250..499 -> 500
            in 500..999 -> 1000
            else -> 10000
        }
    }

}