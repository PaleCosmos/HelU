package com.pale_cosmos.helu

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class FriendAddDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_friend_add)
        window.setFeatureDrawableResource(Window.FEATURE_NO_TITLE, android.R.drawable.ic_dialog_alert)

    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }

    }

    override fun onBackPressed() {
        finish()
        return
    }
}