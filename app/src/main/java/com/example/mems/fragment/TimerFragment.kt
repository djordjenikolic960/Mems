package com.example.mems.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mems.R
import com.example.mems.util.FragmentHelper
import com.example.mems.util.PreferencesHelper
import kotlinx.android.synthetic.main.fragment_timer.*
import java.text.SimpleDateFormat
import java.util.*

class TimerFragment : Fragment() {
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var fragmentHelper: FragmentHelper
    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesHelper = PreferencesHelper(requireContext())
        fragmentHelper = FragmentHelper(requireActivity())
        if (fragmentHelper.isFragmentVisible(TimerFragment::class.java)) {
            handler = Handler(Looper.getMainLooper())
            val post = handler.post(object : Runnable {
                override fun run() {
                    if (fragmentHelper.isFragmentVisible(TimerFragment::class.java)) {
                        val endTime = Calendar.getInstance()
                        val startTime = preferencesHelper.getDateStartTime()
                        val diff = endTime.timeInMillis - startTime
                        if (timer != null) {
                            timer.text = getTimeFormatted(diff)
                        }
                    }
                    handler.postDelayed(this, 1000)
                }
            })
        } else {
            if (::handler.isInitialized) {
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    private fun getTimeFormatted(millisUntilFinished: Long): String {
        val seconds: Long = millisUntilFinished / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val years = days / 365
        return java.lang.StringBuilder().append("Zajedno smo: ").append(String.format("%1$02d", days)).append(" dana, ")
            .append(String.format("%1$01d", hours % 24)).append(" sati, ").append(String.format("%1$01d", minutes % 60)).append(" minuta i ")
            .append(String.format("%1$02d", seconds % 60)).append(" sekundi").toString()
    }
}