package com.example.seasonalmusic

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private var isRunning = false
    private var currentSeason = 0 // 0 for spring, 1 for summer, 2 for autumn, 3 for winter
    private val handler = android.os.Handler()
    private lateinit var bottomFragment: BottomFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        animateImages()
        changeBackgroundColor()

        findViewById<Button>(R.id.startbutton).setOnClickListener {
            if (!isRunning) {
                isRunning = true
                bottomFragment.startSeasonalChanges()
                startSeasonalChanges()
            }
        }

        if (savedInstanceState == null) {
            bottomFragment = BottomFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView2, bottomFragment)
                .commit()
            playMusic(1)
            currentSeason = 1
            isRunning = true
            startSeasonalChanges()
        } else {
            bottomFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as BottomFragment
        }

        findViewById<Button>(R.id.stopbutton).setOnClickListener {
            stopSeasonalChanges()
        }
    }

    private fun stopSeasonalChanges() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun startSeasonalChanges() {
        handler.post(object : Runnable {
            override fun run() {
                if (isRunning) {
                    when (currentSeason) {
                        0 -> {
                            setSeason(BottomFragment.SPRING)
                            playMusic(1)
                        }
                        1 -> {
                            setSeason(BottomFragment.SUMMER)
                            playMusic(2)
                        }
                        2 -> {
                            setSeason(BottomFragment.AUTUMN)
                            playMusic(3)
                        }
                        3 -> {
                            setSeason(BottomFragment.WINTER)
                            playMusic(4)
                        }
                    }
                    currentSeason = (currentSeason + 1) % 4
                    handler.postDelayed(this, 15000) // 15 seconds
                }
            }
        })
    }

    private fun setSeason(season: Int) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        val fragment = BottomFragment()

        val args = Bundle()
        args.putInt("season", season)
        fragment.arguments = args

        transaction.replace(R.id.fragmentContainerView2, fragment)
        transaction.commit()
    }

    private fun playMusic(musicFileNumber: Int) {
        val mediaPlayer = MediaPlayer()

        val musicResourceId = when (musicFileNumber) {
            1 -> R.raw.spring_song
            2 -> R.raw.summer_song
            3 -> R.raw.autumn_song
            4 -> R.raw.winter_song
            else -> R.raw.spring_song // Default music if the musicFileNumber is invalid
        }

        mediaPlayer.setDataSource(resources.openRawResourceFd(musicResourceId))
        mediaPlayer.setOnPreparedListener { mp ->
            mp.start()
        }
        mediaPlayer.setOnCompletionListener { mp ->
            mp.release() // Release the MediaPlayer resources after music finishes
        }

        mediaPlayer.prepareAsync()
    }

    private fun animateImages() {
        val sunImage = findViewById<ImageView>(R.id.sun)
        val birdImage = findViewById<ImageView>(R.id.bird)
        val cloudImage = findViewById<ImageView>(R.id.cloud)

        // Animate the sun image
        val sunAnimator = ObjectAnimator.ofFloat(sunImage, "rotation", 0f, 360f)
        sunAnimator.repeatCount = ObjectAnimator.INFINITE
        sunAnimator.duration = 5000 // 5 seconds
        sunAnimator.interpolator = LinearInterpolator()
        sunAnimator.start()

        // Animate the bird image
        val birdAnimator = ObjectAnimator.ofFloat(birdImage, "translationX", -200f, 200f)
        birdAnimator.repeatCount = ObjectAnimator.INFINITE
        birdAnimator.duration = 3000 // 3 seconds
        birdAnimator.interpolator = LinearInterpolator()
        birdAnimator.start()

        // Animate the cloud image
        val cloudAnimator = ObjectAnimator.ofFloat(cloudImage, "translationX", -300f, 300f)
        cloudAnimator.repeatCount = ObjectAnimator.INFINITE
        cloudAnimator.duration = 7000 // 7 seconds
        cloudAnimator.interpolator = LinearInterpolator()
        cloudAnimator.start()
    }

    private fun changeBackgroundColor() {
        val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)

        val colorAnim = ValueAnimator.ofArgb(
            ContextCompat.getColor(this, R.color.sky_blue),
            ContextCompat.getColor(this, R.color.darkblue_sky)
        )
        colorAnim.duration = 2000.toLong()
        colorAnim.repeatCount = ValueAnimator.INFINITE
        colorAnim.repeatMode = ValueAnimator.REVERSE

        colorAnim.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            rootLayout.setBackgroundColor(color)
        }

        colorAnim.start()
    }

    private fun restartApp() {
        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}





















//package com.example.seasonalmusic
//
//import android.animation.AnimatorSet
//import android.animation.ObjectAnimator
//import android.animation.ValueAnimator
//import android.graphics.drawable.ColorDrawable
//import android.graphics.drawable.TransitionDrawable
//import android.media.MediaPlayer
//
//import android.os.Bundle
//import android.view.View
//import android.view.animation.LinearInterpolator
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.RelativeLayout
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
//
//class MainActivity : AppCompatActivity() {
//
//    private var isRunning = false
//    private var currentSeason = 0 // 0 for spring, 1 for summer, 2 for autumn, 3 for winter
//    private val handler = android.os.Handler()
//    private lateinit var bottomFragment: BottomFragment
//    private lateinit var currentFragmentView: View
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        animateImages()
//        changeBackgroundColor()
//        findViewById<Button>(R.id.startbutton).setOnClickListener {
//            if (!isRunning) {
//                isRunning = true
//                bottomFragment.startSeasonalChanges()
//                startSeasonalChanges()
//            }
//        }
//        if (savedInstanceState == null) {
//            bottomFragment = BottomFragment()
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragmentContainerView2, bottomFragment)
//                .commit()
//           // currentFragmentView = bottomFragment.requireView()
//            // Start with Spring season initially
////            bottomFragment.updateSeasonViews(currentFragmentView,BottomFragment.SPRING)
//            playMusic(1)
//            currentSeason = 1
//            isRunning = true
//            startSeasonalChanges()
//        } else {
//            // Retrieve the existing BottomFragment instance
//            bottomFragment =
//                supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as BottomFragment
//            currentFragmentView = bottomFragment.requireView()
//        }
//        findViewById<Button>(R.id.stopbutton).setOnClickListener {
//            isRunning = false
//            stopSeasonalChanges()
//            handler.removeCallbacksAndMessages(null)
//            //stopMusic()
//
//        }
//
//    }
//    private fun stopSeasonalChanges() {
//        bottomFragment.stopSeasonalImageChange()
//    }
//    private fun startSeasonalChanges() {
//        handler.post(object : Runnable {
//            override fun run() {
//                if (isRunning) {
//                    when (currentSeason) {
//                        0 -> {
//                            setSeason(BottomFragment.SPRING)
//                            playMusic(1)
//                            currentSeason++
//                        }
//                        1 -> {
//                            setSeason(BottomFragment.SUMMER)
//                            playMusic(2)
//                            currentSeason++
//                        }
//                        2 -> {
//                            setSeason(BottomFragment.AUTUMN)
//                            playMusic(3)
//                            currentSeason++
//                        }
//                        3 -> {
//                            setSeason(BottomFragment.WINTER)
//                            playMusic(4)
//                            currentSeason = 0
//                        }
//                    }
//                    handler.postDelayed(this, 15000) // 15 seconds
//                }
//            }
//        })
//    }
//
//    private fun setSeason(season: Int) {
//        val fragmentManager = supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//
//        val fragment = BottomFragment()
//
//        val args = Bundle()
//        args.putInt("season", season)
//        fragment.arguments = args
//
//        transaction.replace(R.id.fragmentContainerView2, fragment)
//        transaction.commit()
//    }
//
//    private fun playMusic(musicFileNumber: Int) {
//        val mediaPlayer = MediaPlayer()
//
//        val musicResourceId = when (musicFileNumber) {
//            1 -> R.raw.spring_song
//            2 -> R.raw.summer_song
//            3 -> R.raw.autumn_song
//            4 -> R.raw.winter_song
//            else -> R.raw.spring_song // Default music if the musicFileNumber is invalid
//        }
//
//        mediaPlayer.setDataSource(resources.openRawResourceFd(musicResourceId))
//        mediaPlayer.setOnPreparedListener { mp ->
//            mp.start()
//        }
//        mediaPlayer.setOnCompletionListener { mp ->
//            mp.release() // Release the MediaPlayer resources after music finishes
//        }
//
//        mediaPlayer.prepareAsync()
//    }
//    private fun animateImages() {
//        val sunImage = findViewById<ImageView>(R.id.sun)
//        val birdImage = findViewById<ImageView>(R.id.bird)
//        val cloudImage = findViewById<ImageView>(R.id.cloud)
//
//        // Animate the sun image
//        val sunAnimator = ObjectAnimator.ofFloat(sunImage, "rotation", 0f, 360f)
//        sunAnimator.repeatCount = ObjectAnimator.INFINITE
//        sunAnimator.duration = 5000 // 5 seconds
//        sunAnimator.interpolator = LinearInterpolator()
//        sunAnimator.start()
//
//        // Animate the bird image
//        val birdAnimator = ObjectAnimator.ofFloat(birdImage, "translationX", -200f, 200f)
//        birdAnimator.repeatCount = ObjectAnimator.INFINITE
//        birdAnimator.duration = 3000 // 3 seconds
//        birdAnimator.interpolator = LinearInterpolator()
//        birdAnimator.start()
//
//        // Animate the cloud image
//        val cloudAnimator = ObjectAnimator.ofFloat(cloudImage, "translationX", -300f, 300f)
//        cloudAnimator.repeatCount = ObjectAnimator.INFINITE
//        cloudAnimator.duration = 7000 // 7 seconds
//        cloudAnimator.interpolator = LinearInterpolator()
//        cloudAnimator.start()
//
//    }
//
//
//   private fun changeBackgroundColor(){
//       val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)
//
//       val colorAnim = ValueAnimator.ofArgb(
//           ContextCompat.getColor(this, R.color.sky_blue),
//           ContextCompat.getColor(this, R.color.darkblue_sky)
//       )
//       colorAnim.duration = 2000.toLong()
//       colorAnim.repeatCount = ValueAnimator.INFINITE
//       colorAnim.repeatMode = ValueAnimator.REVERSE
//
//       colorAnim.addUpdateListener { animator ->
//           val color = animator.animatedValue as Int
//           rootLayout.setBackgroundColor(color)
//       }
//
//       colorAnim.start()
//    }
//}
