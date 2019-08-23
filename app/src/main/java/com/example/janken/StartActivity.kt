package com.example.janken

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        imageView.setOnClickListener{
            delayIntent(it)
        }
        startText.setOnClickListener{
            delayIntent(it)
        }
        imageView2.setOnClickListener{
            delayIntent(it)
        }
    }

    override fun onResume() {
        super.onResume()
        startText.blink()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        setVolumeControlStream(AudioManager.STREAM_MUSIC)
        soundResId = soundPool.load(this, R.raw.hondaplaysound, 1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }

    fun View.blink(times: Int = Animation.INFINITE, du: Long = 200L){
        startAnimation(AlphaAnimation(0.0f, 1.0f).apply {
            duration = du
            startOffset = 20L
            repeatMode = Animation.REVERSE
            repeatCount = times
        })
    }

    fun delayIntent(view: View?){
        view?.setEnabled(false)
        soundPool.play(soundResId, 1.0f, 1.0f, 1, 0, 1.0f)
        startText.blink(30, 10L)
        Handler().postDelayed(Runnable {
            view?.setEnabled(true)
            soundPool.release()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        },2000)
    }
}