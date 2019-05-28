package com.pale_cosmos.helu

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.view.Window.FEATURE_NO_TITLE
import android.content.pm.ActivityInfo
import android.os.Build
import android.widget.Toast
import com.pale_cosmos.helu.util.myUtil
import kotlinx.android.synthetic.main.license.*


class LicenseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        requestWindowFeature(FEATURE_NO_TITLE)

        setContentView(R.layout.license)
        window.setFeatureDrawableResource(FEATURE_NO_TITLE, android.R.drawable.ic_dialog_alert)

        myUtil.buttonSetter(applicationContext,bt1,bt1I,txtText,licenseText,getString(R.string.AVLoadingIndicatorView),"AVLoadingIndicatiorView",
            "https://github.com/81813780/AVLoadingIndicatorView")
        myUtil.buttonSetter(applicationContext,bt2,bt2I,txtText,licenseText,getString(R.string.AndroidWeekView),"Android Week View",
            "https://github.com/alamkanak/Android-Week-View")
        myUtil.buttonSetter(applicationContext,bt3,bt3I,txtText,licenseText,getString(R.string.ChatMessageView),"Chat Message View",
            "https://github.com/bassaer/ChatMessageView")
        myUtil.buttonSetter(applicationContext,bt4,bt4I,txtText,licenseText,getString(R.string.AndroidImageCropper),"Android Image Cropper",
            "https://github.com/ArthurHub/Android-Image-Cropper")
        myUtil.buttonSetter(applicationContext,bt5,bt5I,txtText,licenseText,getString(R.string.TedPermission),"Ted Permission",
            "https://github.com/ParkSangGwon/TedPermission")
        myUtil.buttonSetter(applicationContext,bt6,bt6I,txtText,licenseText,getString(R.string.Glide),"Glide",
            "https://github.com/ParkSangGwon/TedPermission")

    }



    override fun onBackPressed() {
        finish()
        return
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }

    }
}




