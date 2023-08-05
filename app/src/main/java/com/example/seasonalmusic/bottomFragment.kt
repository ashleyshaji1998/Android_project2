package com.example.seasonalmusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.view.animation.LinearInterpolator
import java.util.*

class BottomFragment : Fragment() {

    companion object {
        const val SPRING = 0
        const val SUMMER = 1
        const val AUTUMN = 2
        const val WINTER = 3
    }
    private var isRunning = false

    private var season = SPRING
    private val handler = Handler()
//    private val seasonalImages = listOf(
//        R.drawable.spring,
//        R.drawable.summer,
//        R.drawable.autumn,
//        R.drawable.winter
//    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom, container, false)

        arguments?.let {
            season = it.getInt("season", SPRING)
        }

        // Update the views based on the season
        updateSeasonViews(view, season)
        startSeasonalImageChange()

        return view
    }

    override fun onResume() {
        super.onResume()
        if (isRunning) {
            startSeasonalImageChange()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }
    fun startSeasonalChanges() {
        isRunning = true
        startSeasonalImageChange()
    }

    fun stopSeasonalImageChange() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }
    private fun startSeasonalImageChange() {
        handler.post(object : Runnable {
            override fun run() {
                when (season) {
                    SPRING -> season = SUMMER
                    SUMMER -> season = AUTUMN
                    AUTUMN -> season = WINTER
                    WINTER -> season = SPRING
                }

                // Update the seasonal image view with the new season
                view?.let { updateSeasonViews(it, season) }
                view?.let { rotatewheel(it)}
                // Repeat the process every 10 seconds
                handler.postDelayed(this, 10000) // 10 seconds
            }
        })
    }

     fun updateSeasonViews(view: View, season: Int) {
        val imageView: ImageView = view.findViewById(R.id.seasonal)
        val textView: TextView = view.findViewById(R.id.date_text_view)
        val rootView: View = view.findViewById(R.id.fragment_root_view)
        // Update the views based on the current season
        when (season) {
            SPRING -> {
                // Apply spring animations and images
                rootView.setBackgroundColor(Color.parseColor("#FF4500"))
                imageView.setImageResource(R.drawable.spring)
              //  textView.text = "Spring Time"
              //  animateSpring(imageView)
            }
            SUMMER -> {
                // Apply summer animations and images
                rootView.setBackgroundColor(Color.parseColor("#8FBC8F"))
                imageView.setImageResource(R.drawable.summer)
               // textView.text = "Summer Time"
            }
            AUTUMN -> {
                // Apply autumn animations and images
                rootView.setBackgroundColor(Color.parseColor("#FFFF00"))
                imageView.setImageResource(R.drawable.autumn)
               // textView.text = "Autumn Time"
            }
            WINTER -> {
                // Apply winter animations and images
                rootView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                imageView.setImageResource(R.drawable.winter)
               // textView.text = "Winter Time"
            }
        }

        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        textView.text = " $currentDateTime"
    }
    private fun rotatewheel(view: View)
    {
        val wheelImageView: ImageView = view.findViewById(R.id.wheel_image_view)
       // val wheelImageView = view.findViewById<ImageView>(R.id.wheel_image_view)
        val wheelAnimator = ObjectAnimator.ofFloat(wheelImageView, "rotation", 0f, 360f)
        wheelAnimator.repeatCount = ObjectAnimator.INFINITE
        wheelAnimator.duration = 8000 // 8 seconds
        wheelAnimator.interpolator = LinearInterpolator()
        wheelAnimator.start()
    }




}
