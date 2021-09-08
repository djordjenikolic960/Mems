package com.example.mems.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.mems.MainActivity
import com.example.mems.R
import kotlinx.android.synthetic.main.view_content_main.*

object StyleUtil {
    fun stylizeStatusBar(activity: Activity, transparent: Boolean) {
        if (transparent) {
            val layoutParams: CoordinatorLayout.LayoutParams = activity.appBar.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.topMargin = 0
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.decorView.systemUiVisibility =
                (activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        } else {
            val layoutParams: CoordinatorLayout.LayoutParams = activity.appBar.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.topMargin = -getStatusBarHeight(activity)
            activity.window.statusBarColor = (activity as MainActivity).resources.getColor(R.color.black)
            activity.window.decorView.systemUiVisibility =
                (activity.window.decorView.systemUiVisibility and (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN).inv())
        }
    }

     fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    @JvmStatic
    fun getAttributeColor(context: Context, @AttrRes resId: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(resId, typedValue, true)
        return typedValue.data
    }
}