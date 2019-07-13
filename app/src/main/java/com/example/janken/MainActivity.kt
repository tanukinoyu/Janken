package com.example.janken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gu.setOnClickListener{onJankenButtonTapped(it)}
        choki. setOnClickListener{onJankenButtonTapped(it)}
        pa.setOnClickListener{onJankenButtonTapped(it)}

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit {
            clear()
        }
    }

    override fun onStart() {
        super.onStart()
        getCount()
    }


    fun onJankenButtonTapped(view: View?){
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("MY_HAND", view?.id)
        startActivity(intent)
    }

    fun getCount(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val totalGameCount = pref.getInt("TOTAL_GAME_COUNT", 0)
        val winCount = pref.getInt("WIN_COUNT", 0)
        val winRate = ((winCount.toDouble() / totalGameCount.toDouble()) * 100).toInt()
        val myWinningStreakCount = pref.getInt("MY_WINNING_STREAK_COUNT", 0)
        val maxWinningStreakCount = pref.getInt("MAX_WINNING_STREAK_COUNT", 0)

        totalGameCountText.text = "総試合数　${totalGameCount}　回"
        winCountText.text = "勝利数　${winCount}　回"
        winRateText.text = "勝率　${winRate}　％"
        myWinningStreakCountText.text = "連勝数　${myWinningStreakCount}　回"
        maxWinningStreakCountText.text = "最大連勝数　${maxWinningStreakCount}　回"

    }
}
