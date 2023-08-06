package com.example.seasonalmusic

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.media.MediaPlayer

import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator

class MainActivity : AppCompatActivity() {

    private var isRunning = true
    private var currentSeason = 0 // 0 for spring, 1 for summer, 2 for autumn, 3 for winter
    private val handler = android.os.Handler()
    private lateinit var bottomFragment: BottomFragment
    private lateinit var currentFragmentView: View
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusicFileNumber = 1
    private var isSeasonalChangeRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        animateImages()
        changeBackgroundColor()
        startSeasonalChanges()
        findViewById<Button>(R.id.startbutton).setOnClickListener {
            if (!isRunning) {
                isRunning = true
                currentSeason = BottomFragment.SPRING
                if (::bottomFragment.isInitialized) {
                    bottomFragment.startSeasonalChanges()
                } else {
                    bottomFragment = BottomFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragmentContainerView2, bottomFragment)
                        .commit()
                }
                startSeasonalChanges()
                playMusic(currentMusicFileNumber)
            }
        }
        if (savedInstanceState == null) {
            bottomFragment = BottomFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView2, bottomFragment)
                .commit()
        } else {
            bottomFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as BottomFragment
            currentFragmentView = bottomFragment.requireView()
        }
        findViewById<Button>(R.id.stopbutton).setOnClickListener {
            isRunning = false
            stopSeasonalChanges()
            stopMusic()
            bottomFragment.stopSeasonalImageChange()
            handler.removeCallbacksAndMessages(null)
        }
    }
    private fun startSeasonalChanges() {
        isSeasonalChangeRunning = true

        currentSeason = 3
       // playMusic(4)
        handler.post(object : Runnable {
            override fun run() {
                if (isRunning && isSeasonalChangeRunning) {
                    when (currentSeason) {
                        0 -> {
                            setSeason(BottomFragment.SPRING)
                            playMusic(2)
                            currentSeason++
                        }
                        1 -> {
                            setSeason(BottomFragment.SUMMER)
                            playMusic(3)
                            currentSeason++
                        }
                        2 -> {
                            setSeason(BottomFragment.AUTUMN)
                            playMusic(4)
                            currentSeason++
                        }
                        3 -> {
                            setSeason(BottomFragment.WINTER)
                            playMusic(1)
                            currentSeason = 0
                        }
                    }
                    handler.postDelayed(this, 15000) // 15 seconds
                }
                else {
                    handler.removeCallbacks(this)
                }
            }
        })
    }
    private fun stopSeasonalChanges() {
        isSeasonalChangeRunning = false
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
        mediaPlayer?.apply {
        if (isPlaying) {
            stop()
            release()
        }
    }

        val musicResourceId = when (musicFileNumber) {
            1 -> R.raw.spring_song
            2 -> R.raw.summer_song
            3 -> R.raw.autumn_song
            4 -> R.raw.winter_song
            else -> return
        }
        mediaPlayer = MediaPlayer.create(this, musicResourceId)
        mediaPlayer?.setOnCompletionListener {
            // Start playing the music for the next season
            when (currentMusicFileNumber) {
                1 -> playMusic(1)
                2 -> playMusic(2)
                3 -> playMusic(3)
                4 -> playMusic(4)
            }
        }
        mediaPlayer?.start()
        currentMusicFileNumber = musicFileNumber

    }
    private fun stopMusic() {
        mediaPlayer?.apply {
            if (isPlaying) {
                pause()
            }

        }
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
   private fun changeBackgroundColor(){
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
}
