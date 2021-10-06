package com.example.mems.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.mems.R

class DaresAdapter(val context: Context) : PagerAdapter() {
    private lateinit var daresPage: View

    companion object {
        const val DARES_SCORE_PAGE_POSITION = 0
        const val DARES_SEND_PAGE_POSITION = 1
        const val DARES_PAGES_COUNT = 3
    }

    override fun getCount(): Int {
        return DARES_PAGES_COUNT
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        daresPage = when (position) {
            DARES_SCORE_PAGE_POSITION -> {
                layoutInflater.inflate(R.layout.dares_score_view, container, false)
            }
            DARES_SEND_PAGE_POSITION -> {
                layoutInflater.inflate(R.layout.dares_send_view, container, false)
            }
            else -> {
                layoutInflater.inflate(R.layout.dares_received_view, container, false)
            }
        }
        container.addView(daresPage)
        return daresPage
    }
}