package com.example.mems.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mems.R
import com.example.mems.util.FragmentHelper
import com.example.mems.util.PreferencesHelper
import kotlinx.android.synthetic.main.fragment_timer.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*

class CounterFragment : Fragment() {
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
        if (fragmentHelper.isFragmentVisible(CounterFragment::class.java)) {
            handler = Handler(Looper.getMainLooper())
            val post = handler.post(object : Runnable {
                override fun run() {
                    if (fragmentHelper.isFragmentVisible(CounterFragment::class.java)) {
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
        val konfettiView = viewKonfetti
        konfettiView.setOnClickListener {
            konfettiView.build()
                .addColors(Color.rgb(248, 72, 72), Color.rgb(81, 138, 255), Color.rgb(255, 139, 14), Color.rgb(255, 201, 14), Color.rgb(12, 182, 24))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(12), Size(5), Size(10))
                .setPosition(-50f, konfettiView.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
        }
    }

    private fun getTimeFormatted(millisUntilFinished: Long): String {
        val seconds: Long = millisUntilFinished / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val years = days / 365
        return if (years <= 0) {
            java.lang.StringBuilder().append(String.format("%1$02d", days)).append(" days, ")
                .append(String.format("%1$01d", hours % 24)).append(" hours, ").append(String.format("%1$01d", minutes % 60)).append(" minutes and ")
                .append(String.format("%1$02d", seconds % 60)).append(" seconds").toString()
        } else {
            java.lang.StringBuilder().append(String.format("%1$02d", years)).append(" years, ").append(String.format("%1$02d", days)).append(" days, ")
                .append(String.format("%1$01d", hours % 24)).append(" hours, ").append(String.format("%1$01d", minutes % 60)).append(" minutes and ")
                .append(String.format("%1$02d", seconds % 60)).append(" seconds").toString()
        }

    }
}