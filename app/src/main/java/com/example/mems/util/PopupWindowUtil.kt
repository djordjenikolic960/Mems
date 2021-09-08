package com.example.mems.util

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.example.mems.R

object PopupWindowUtil {

    fun getPopupWindow(context: Context, layout: Int): PopupWindow {
        val popupWindow = PopupWindow(context)
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(layout, null)
        popupWindow.isFocusable = false
        popupWindow.width = WindowManager.LayoutParams.MATCH_PARENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.setBackgroundDrawable(ColorDrawable(StyleUtil.getAttributeColor(context, R.attr.colorBackgroundFloating)))
        popupWindow.contentView = view
        return popupWindow
    }
}