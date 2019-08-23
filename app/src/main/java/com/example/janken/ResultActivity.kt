package com.example.janken

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_result.*
import javax.xml.parsers.SAXParser

class ResultActivity : AppCompatActivity() {

    val gu = 0
    val choki = 1
    val pa = 2

    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("MY_HAND", 0)
        val atodashi = intent.getBooleanExtra("ATODASHI", false)

        val myHand: Int
        myHand = when(id){
            R.id.gu -> {
                myHandImage.setImageResource(R.drawable.com_gu)
                gu
            }
            R.id.choki -> {
                myHandImage.setImageResource(R.drawable.com_choki)
                choki
            }
            R.id.pa -> {
                myHandImage.setImageResource(R.drawable.com_pa)
                pa
            } else -> {
                gu
            }
        }

        val comHand = getHand(myHand, atodashi)

        val gameResult = (comHand - myHand + 3) % 3

        var fragment: Fragment

        if (atodashi){
            fragment = when(myHand){
                gu -> ChokiFragment()
                pa -> GuFragment()
                else -> PaFragment()
            }
            getFragment(fragment)
            Handler().postDelayed(Runnable {
                soundPool.play(soundResId,1.0f, 1.0f, 1, 0, 1.0f)
                resultLabel.setText(R.string.result_lose)
                fragment = when(myHand){
                    gu -> PaVideoFragment()
                    pa -> ChokiVideoFragment()
                    else-> GuVideoFragment()
                }
                getFragment(fragment)
            },1000)
        } else {
            when(gameResult){
                0 -> resultLabel.setText(R.string.result_draw)
                1 -> resultLabel.setText(R.string.result_win)
                2 -> resultLabel.setText(R.string.result_lose)
            }
            fragment = when(gameResult){
                0 -> when(myHand){
                    gu -> GuFragment()
                    pa -> PaFragment()
                    else -> ChokiFragment()
                }

                1 -> when(myHand){
                    gu -> ChokiFragment()
                    pa -> GuFragment()
                    else -> PaFragment()
                }
                2 -> when(myHand){
                    gu -> PaVideoFragment()
                    pa -> ChokiVideoFragment()
                    else -> GuVideoFragment()
                }
                else -> GuFragment()
            }
            getFragment(fragment)
            if(gameResult == 1){
                Handler().postDelayed(Runnable {
                    fragment = WinVideoFragment()
                    getFragment(fragment)
                }, 1000)
            }
        }

        saveData(myHand, comHand, gameResult)
    }

    override fun onResume() {
        super.onResume()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        setVolumeControlStream(AudioManager.STREAM_MUSIC)
        soundResId = soundPool.load(this, R.raw.hondapon, 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    fun getFragment(fragment: Fragment){
        val fragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction().apply {
            this.replace(R.id.frameLayout, fragment)
            this.commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun saveData(myHand: Int, comHand: Int, gameResult: Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val lastGameResult = pref.getInt("GAME_RESULT", -1)

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
            putInt("GAME_RESULT", gameResult) }
    }

    private fun getHand(myHand: Int, atodashi: Boolean): Int {
        var hand = (Math.random() * 3).toInt()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val lastMyHand = pref.getInt("LAST_MY_HAND", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND", 0)
        val gameResult = pref.getInt("GAME_RESULT", -1)

        if (atodashi){
            hand = (myHand + 2) % 3
        } else{
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
        }

        return hand
    }

}
