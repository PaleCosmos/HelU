package com.pale_cosmos.helu
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager

class SplashActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

       updateStatusBarColor("#E43F3F")
        setContentView(R.layout.splash)
        var h = Handler()
       // (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(1000)
        h.postDelayed(SplashHandler(), 2000)

    }
    inner class SplashHandler:Runnable{
        override fun run() {
            var intent = Intent(applicationContext, LoginActivity::class.java)

            startActivityForResult(intent,400)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            finish()


        }
    }
    private fun updateStatusBarColor(color: String) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor(color)
        }
    }
}