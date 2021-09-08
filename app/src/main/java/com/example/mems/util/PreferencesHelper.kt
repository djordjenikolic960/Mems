package com.example.mems.util

import android.content.Context
import android.preference.PreferenceManager
import java.time.Month
import java.util.*

class PreferencesHelper(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getDateStartTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2021)
        calendar.set(Calendar.MONTH, 6)
        calendar.set(Calendar.DAY_OF_MONTH, 12)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return preferences.getLong("dateStartTime", calendar.timeInMillis)
    }
}