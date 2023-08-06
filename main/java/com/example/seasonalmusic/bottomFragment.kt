package com.example.seasonalmusic

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
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

    private var season = 0
    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom, container, false)
    startDateTimeUpdate(view)
        arguments?.let {
            season = it.getInt("season", SPRING)
        }
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

    private fun startDateTimeUpdate(view: View) {
        val dateTextView: TextView = view.findViewById(R.id.date_text_view)
        handler.post(object : Runnable {
            override fun run() {
                val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                dateTextView.text = currentDateTime
                handler.postDelayed(this, 1000) // 1 second
            }
        })
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
                handler.postDelayed(this, 15000) // 10 seconds
            }
        })
    }

     fun updateSeasonViews(view: View, season: Int) {
         val imageView: ImageView = view.findViewById(R.id.seasonal)

         val rootView: View = view.findViewById(R.id.fragment_root_view)

         // Update the views based on the current season
         when (season) {
             SPRING -> {
                 // Apply spring animations and images
                 fadeBackgroundColor(rootView, Color.parseColor("#FF4500"))
                 fadeImage(imageView, R.drawable.spring)
             }
             SUMMER -> {
                 // Apply summer animations and images
                 fadeBackgroundColor(rootView, Color.parseColor("#8FBC8F"))
                 fadeImage(imageView, R.drawable.summer)
             }
             AUTUMN -> {
                 // Apply autumn animations and images
                 fadeBackgroundColor(rootView, Color.parseColor("#FFFF00"))
                 fadeImage(imageView, R.drawable.autumn)
             }
             WINTER -> {
                 // Apply winter animations and images
                 fadeBackgroundColor(rootView, Color.parseColor("#FFFFFF"))
                 fadeImage(imageView, R.drawable.winter)
             }
         }
     }

    private fun fadeBackgroundColor(view: View, targetColor: Int) {
        val colorAnimator = ObjectAnimator.ofArgb(view, "backgroundColor", (view.background as ColorDrawable).color, targetColor)
        colorAnimator.duration = 500 // 0.5 seconds
        colorAnimator.start()
    }

    private fun fadeImage(imageView: ImageView, imageResId: Int) {
        val fadeOutAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f)
        fadeOutAnimator.duration = 250 // 0.25 seconds
        fadeOutAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                imageView.setImageResource(imageResId)
                val fadeInAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)
                fadeInAnimator.duration = 250 // 0.25 seconds
                fadeInAnimator.start()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        fadeOutAnimator.start()
    }
    private fun rotatewheel(view: View)
    {
        val wheelImageView: ImageView = view.findViewById(R.id.wheel_image_view)
       // val wheelImageView = view.findViewById<ImageView>(R.id.wheel_image_view)
        val wheelAnimator = ObjectAnimator.ofFloat(wheelImageView, "rotation", 0f, 360f)
        wheelAnimator.repeatCount = ObjectAnimator.INFINITE
        wheelAnimator.duration = 15000 // 8 seconds
        wheelAnimator.interpolator = LinearInterpolator()
        wheelAnimator.start()
    }
}
