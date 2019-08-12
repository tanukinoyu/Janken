package com.example.janken

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
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

        startText.blink()

        imageView.setOnClickListener{
            soundPool.play(soundResId, 1.0f, 1.0f, 1, 0, 1.0f)
            delayIntent(this,savedInstanceState)
        }
        startText.setOnClickListener{
            soundPool.play(soundResId, 1.0f, 1.0f, 1, 0, 1.0f)
            delayIntent(this,savedInstanceState)
        }
        imageView2.setOnClickListener{
            soundPool.play(soundResId, 1.0f, 1.0f, 1, 0, 1.0f)
            delayIntent(this,savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        soundResId = soundPool.load(this, R.raw.hondaplaysound, 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}

fun View.blink(times: Int = Animation.INFINITE){
    startAnimation(AlphaAnimation(0.0f, 1.0f).apply {
        duration = 200L
        startOffset = 20L
        repeatMode = Animation.REVERSE
        repeatCount = times
    })
}

fun delayIntent(context: Context,savedInstanceState: Bundle?){
    Toast.makeText(context, "GAME START !!", Toast.LENGTH_SHORT).also {
        it.setGravity(Gravity.CENTER, 0, 0)
        it.setMargin(10.0f, 10.0f)
        it.show()
    }
    Handler().postDelayed(Runnable {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context,intent,savedInstanceState)
    },2000)
}