package com.example.halilarm

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.Window
import android.widget.Button

class LogoutActivity : AppCompatActivity() {
    var yes: Button? = null
    var no: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.logout)
        window.setFeatureDrawableResource(Window.FEATURE_NO_TITLE, android.R.drawable.ic_dialog_alert)
        yes = findViewById(R.id.bbb1)
        no = findViewById(R.id.bbb2)
        yes?.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
        no?.setOnClickListener { finish() }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 바깥 클릭하면 안닫히게
        // 레지스터 이벤트 조정 필요
        return super.onTouchEvent(event)
    }

    override fun onBackPressed() {
        finish()
        return
    }
}