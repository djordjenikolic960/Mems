package com.example.mems

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mems.fragment.BucketListFragment
import com.example.mems.fragment.MemsFragment
import com.example.mems.fragment.TimerFragment
import com.example.mems.fragment.UsFragment
import com.example.mems.util.FragmentHelper
import com.example.mems.util.StyleUtil
import com.example.mems.util.StyleUtil.getStatusBarHeight
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.view_bottom_navigation.*
import kotlinx.android.synthetic.main.view_content_main.*
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_GALLERY_PHOTO = 2
    }

    private lateinit var fragmentHelper: FragmentHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if (!haveStoragePermission()) {
                requestPermission()
            } else {
                fragmentHelper.replaceFragment(MemsFragment::class.java)
            }
        }
        timerLayout.setOnClickListener { fragmentHelper.replaceFragment(TimerFragment::class.java) }
        usLayout.setOnClickListener { fragmentHelper.replaceFragment(UsFragment::class.java) }
        bucketListLayout.setOnClickListener { fragmentHelper.replaceFragment(BucketListFragment::class.java) }
    }

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(
                this@MainActivity,
                permissions,
                REQUEST_GALLERY_PHOTO
            )
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_GALLERY_PHOTO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fragmentHelper.replaceFragment(MemsFragment::class.java)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.subtitle = null
        fab.visibility = View.GONE
        when {
            fragmentHelper.isFragmentVisible(TimerFragment::class.java) -> {
                supportActionBar!!.title = "Time we are together"
            }
            fragmentHelper.isFragmentVisible(MemsFragment::class.java) -> {
                supportActionBar!!.title = "Loving images we shared"
            }
            fragmentHelper.isFragmentVisible(UsFragment::class.java) -> {
                supportActionBar!!.title = "This is us"
            }
            else -> {
                fab.visibility = View.VISIBLE
                supportActionBar!!.title = "Our Bucket list"
            }
        }

        return super.onCreateOptionsMenu(menu)
    }
}