package com.example.mems

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.example.mems.fragment.*
import com.example.mems.util.FragmentHelper
import com.example.mems.util.StyleUtil
import com.example.mems.util.StyleUtil.getStatusBarHeight
import kotlinx.android.synthetic.main.view_bottom_navigation.*
import kotlinx.android.synthetic.main.view_content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentHelper: FragmentHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Gray)
        setContentView(R.layout.view_content_main)
        StyleUtil.stylizeStatusBar(this@MainActivity, true)
        initHelpers()
        initView()
        initListeners()
        fragmentHelper.initFragment(savedInstanceState)
    }

    private fun initView() {
        val lp = appBar.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = resources.getDimensionPixelSize(R.dimen.toolbar_height) + getStatusBarHeight(this)
        appBar.requestLayout()
    }

    private fun initHelpers() {
        fragmentHelper = FragmentHelper(this)
    }

    private fun initListeners() {
        memsLayout.setOnClickListener {
            if (!fragmentHelper.isFragmentVisible(MemsFragment::class.java)) {
                fragmentHelper.replaceFragment(MemsFragment::class.java)
            }
        }
        timerLayout.setOnClickListener {
            if (!fragmentHelper.isFragmentVisible(CounterFragment::class.java)) {
                fragmentHelper.replaceFragment(CounterFragment::class.java)
            }
        }
        usLayout.setOnClickListener {
            if (!fragmentHelper.isFragmentVisible(LoveScoreFragment::class.java)) {
                fragmentHelper.replaceFragment(LoveScoreFragment::class.java)
            }
        }
        bucketListLayout.setOnClickListener {
            if (!fragmentHelper.isFragmentVisible(BucketListFragment::class.java)) {
                fragmentHelper.replaceFragment(BucketListFragment::class.java)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar!!.setDisplayHomeAsUpEnabled(fragmentHelper.isFragmentVisible(ImageFragment::class.java))
        bottomNavigation.isVisible = !fragmentHelper.isFragmentVisible(ImageFragment::class.java)
        supportActionBar!!.subtitle = null
        fab.visibility = View.GONE
        when {
            fragmentHelper.isFragmentVisible(CounterFragment::class.java) -> {
                supportActionBar!!.title = getString(R.string.counter_fragment_title)
            }
            fragmentHelper.isFragmentVisible(MemsFragment::class.java) -> {
                supportActionBar!!.title = getString(R.string.mems_fragment_title)
            }
            fragmentHelper.isFragmentVisible(LoveScoreFragment::class.java) -> {
                supportActionBar!!.title = getString(R.string.love_score_fragment_title)
            }
            fragmentHelper.isFragmentVisible(BucketListFragment::class.java) -> {
                fab.visibility = View.VISIBLE
                supportActionBar!!.title = getString(R.string.bucket_list_fragment_title)
            }
            else -> {
                supportActionBar!!.title = String()
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (fragmentHelper.isFragmentVisible(ImageFragment::class.java)) {
            fragmentHelper.replaceFragment(MemsFragment::class.java)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (fragmentHelper.isFragmentVisible(ImageFragment::class.java)) {
                fragmentHelper.replaceFragment(MemsFragment::class.java)
            } else {
                super.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}