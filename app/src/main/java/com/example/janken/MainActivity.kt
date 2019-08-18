package com.example.janken

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView.setVideoPath("android.resource://" + this.getPackageName() + "/" + R.raw.hondajanken)
        videoView.start()

        gu.setOnClickListener{onJankenButtonTapped(it)}
        choki. setOnClickListener{onJankenButtonTapped(it)}
        pa.setOnClickListener{onJankenButtonTapped(it)}

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit {
            clear()
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
        soundPool.release()
        soundResId = soundPool.load(this, R.raw.hondapon, 1)
    }

    fun onJankenButtonTapped(view: View?){
        soundPool.play(soundResId, 1.0f, 1.0f, 1, 0, 1.0f)
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("MY_HAND", view?.id)
            .putExtra("ATODASHI", checkBox.isChecked())
        startActivity(intent)
    }
}
