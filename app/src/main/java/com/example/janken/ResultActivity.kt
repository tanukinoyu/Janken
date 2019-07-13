// Hideさんの問題作成用_

package com.example.janken

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_result.*
import java.lang.Thread.sleep

class ResultActivity : AppCompatActivity() {

    val gu = 0
    val choki = 1
    val pa = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val id = intent.getIntExtra("MY_HAND", 0)

        val myHand: Int
        myHand = when(id){
            R.id.gu -> {
                myHandImage.setImageResource(R.drawable.gu)
                gu
            }
            R.id.choki -> {
                myHandImage.setImageResource(R.drawable.choki)
                choki
            }
            R.id.pa -> {
                myHandImage.setImageResource(R.drawable.pa)
                pa
            } else -> {
                gu
            }
        }

        val comHand = getHand()
        when(comHand){
            gu -> comHandImage.setImageResource(R.drawable.com_gu)
            choki -> comHandImage.setImageResource(R.drawable.com_choki)
            pa -> comHandImage.setImageResource(R.drawable.com_pa)
        }

        val gameResult = (comHand - myHand + 3) % 3
        when(gameResult){
            0 -> resultLabel.setText(R.string.result_draw)
            1 -> resultLabel.setText(R.string.result_win)
            2 -> resultLabel.setText(R.string.result_lose)
        }

        backButton.setOnClickListener { finish() }

        saveData(myHand, comHand, gameResult)
    }

    private fun saveData(myHand: Int, comHand: Int, gameResult: Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val lastGameResult = pref.getInt("GAME_RESULT", -1)
        val totalGameCount = pref.getInt("TOTAL_GAME_COUNT", 0)
        val winCount = pref.getInt("WIN_COUNT", 0)
        var myWinningStreakCount = pref.getInt("MY_WINNING_STREAK_COUNT", 0)
        val maxWinningStreakCount = pref.getInt("MAX_WINNING_STREAK_COUNT", 0)

        val edtWinningStreakCount: Int = when{
            lastGameResult == 2 && gameResult == 2 ->
            winningStreakCount + 1
            else -> 0
        }

        pref.edit {
            putInt("GAME_COUNT", gameCount + 1)
            putInt("WINNING_STREAK_COUNT", edtWinningStreakCount)
            putInt("LAST_MY_HAND", myHand)
            putInt("LAST_COM_HAND", comHand)
            putInt("BEFORE_LAST_COM_HAND", lastComHand)
            putInt("GAME_RESULT", gameResult)
            putInt("TOTAL_GAME_COUNT", gameCount + 1)
            putInt("WIN_COUNT",
                if(gameResult == 1)
                    winCount + 1
                else
                    winCount)
            putInt("MY_WINNING_STREAK_COUNT",
                if(gameResult == 1)
                    myWinningStreakCount + 1
                else if(lastGameResult == 1 && gameResult == 0)
                    myWinningStreakCount
                else if(lastGameResult == 0 && gameResult == 0)
                    myWinningStreakCount
                else
                    0
            )
        }
        myWinningStreakCount = pref.getInt("MY_WINNING_STREAK_COUNT", 0)
        pref.edit{
            putInt("MAX_WINNING_STREAK_COUNT",
                if(myWinningStreakCount >= maxWinningStreakCount)
                    myWinningStreakCount
                else
                    maxWinningStreakCount
            )
        }

    }

    private fun getHand(): Int {
        var hand = (Math.random() * 3).toInt()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val lastMyHand = pref.getInt("LAST_MY_HAND", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND", 0)
        val gameResult = pref.getInt("GAME_RESULT", -1)

        textView2.text = gameCount.toString()

        if(gameCount == 1){
            if(gameResult == 2){
                while(lastComHand == hand){
                    hand = (Math.random() * 3).toInt()
                }
            } else if(gameResult == 1){
                hand = (lastMyHand - 1 + 3) % 3
            }
        } else if(winningStreakCount > 0){
            if(beforeLastComHand == lastComHand){
                while(lastComHand == hand){
                    hand = (Math.random() * 3).toInt()
                }
            }
        }

        return hand
    }

}
