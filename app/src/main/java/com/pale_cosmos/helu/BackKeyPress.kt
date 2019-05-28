package com.pale_cosmos.helu


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_back_press.*
import android.os.Build
import android.view.MotionEvent
import com.pale_cosmos.helu.util.myUtil


class BackKeyPress : AppCompatActivity() {

    var CODE: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_back_press)


        myUtil.codeSelect(this@BackKeyPress,intent.getIntExtra("code", -1),qkqh,ccc1,ccc2)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_OUTSIDE) {

        }

        return false
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }

    }
}