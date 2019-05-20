package com.example.halilarm

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import kotlinx.android.synthetic.main.activity_ringing.*

class ringing:AppCompatActivity(){

    private var mediaPlayer:MediaPlayer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_ringing)
        window.setFeatureDrawableResource(Window.FEATURE_NO_TITLE, android.R.drawable.ic_dialog_alert)
        GGOGGO.setOnClickListener {
            mediaPlayer?.stop()
            finish()
        }
mediaPlayer = MediaPlayer.create(this,R.raw.matcha)
        mediaPlayer?.start()

    }

    override fun onBackPressed() {
        mediaPlayer?.stop()
        finish()
    }
}