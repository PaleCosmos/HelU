package com.example.halilarm

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.view.Window.FEATURE_NO_TITLE
import android.content.pm.ActivityInfo
import android.widget.Toast
import kotlinx.android.synthetic.main.license.*


class LicenseActivity : AppCompatActivity() {

    private var txtText: TextView? = null // context
    private var licenseText: TextView? = null // name
    private var button2: Button? = null
    private var button2INV: Button? = null

    private var clips: ClipboardManager? = null
    private var clipData: ClipData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.license)
        window.setFeatureDrawableResource(FEATURE_NO_TITLE, android.R.drawable.ic_dialog_alert)
        txtText = findViewById(R.id.txtText)
        clips = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        licenseText = findViewById(R.id.licenseText)
        bt1?.setOnClickListener {
            txtText?.text = getString(R.string.AVLoadingIndicatorView)
            licenseText?.text = "AVLoadingIndicatorView"
        }

        bt1I?.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/81813780/AVLoadingIndicatorView")))
        }
        bt1I?.setOnLongClickListener {
            clipData = ClipData.newPlainText("", "https://github.com/81813780/AVLoadingIndicatorView")
            clips?.primaryClip = clipData
            clipCopy("AVLoadingIndicatorView")
            true
        }
        button2 = findViewById(R.id.bt2)
        button2?.setOnClickListener {
            txtText?.text = getString(R.string.AndroidWeekView)
            licenseText?.text = "Android Week View"
        }
        button2INV = findViewById(R.id.bt2I)
        button2INV?.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/alamkanak/Android-Week-View")))
        }
        button2INV?.setOnLongClickListener {
            clipData = ClipData.newPlainText("", "https://github.com/alamkanak/Android-Week-View")
            clips?.primaryClip = clipData
            clipCopy("Android Week View")
            true
        }

        bt3.setOnClickListener { txtText?.text = getString(R.string.ChatMessageView)
            licenseText?.text = "Chat Message View"}
        bt3I.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bassaer/ChatMessageView")))

        }
        bt3I.setOnLongClickListener {
            clipData = ClipData.newPlainText("", "https://github.com/bassaer/ChatMessageView")
            clips?.primaryClip = clipData
            clipCopy("Chat Message View")
            true
        }
    }


    private fun clipCopy(name: String?) {
        Toast.makeText(applicationContext, "클립보드에 복사되었습니다 [$name]", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        finish()
        return
    }
}




