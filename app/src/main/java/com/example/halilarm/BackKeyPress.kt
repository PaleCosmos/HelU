package com.example.halilarm

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_back_press.*
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Build



class BackKeyPress :AppCompatActivity(){

    var CODE:Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_back_press)
        if(intent.getIntExtra("code",-1)==0)
        {
            qkqh.text="U chat이 종료됩니다!"
            ccc1.text="나가기"
            ccc2.text="머무르기"
            CODE=0
        }else if(intent.getIntExtra("code",-1)==1)
        {
            qkqh.text="종료할까요?"
            ccc1.text="네"
            ccc2.text="아니요"
            CODE=1
        }
        ccc1.setOnClickListener {

           if(CODE==0)setResult(88)
            if(CODE==1)setResult(99)
            finish()
        }
        ccc2.setOnClickListener { finish() }
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }

    }
}