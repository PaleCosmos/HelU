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
import kotlinx.android.synthetic.main.license.*


class LicenseActivity : AppCompatActivity() {


    private var clips: ClipboardManager? = null
    private var clipData: ClipData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        requestWindowFeature(FEATURE_NO_TITLE)

        setContentView(R.layout.license)
        window.setFeatureDrawableResource(FEATURE_NO_TITLE, android.R.drawable.ic_dialog_alert)

        clips = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        bt1.setOnClickListener {
            txtText.text = getString(R.string.AVLoadingIndicatorView)
            licenseText.text = "AVLoadingIndicatorView"
        }

        bt1I.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/81813780/AVLoadingIndicatorView")))
        }
        bt1I.setOnLongClickListener {
            clipData = ClipData.newPlainText("", "https://github.com/81813780/AVLoadingIndicatorView")
            clips?.primaryClip = clipData
            clipCopy("AVLoadingIndicatorView")
            true
        }

        bt2.setOnClickListener {
            txtText.text = getString(R.string.AndroidWeekView)
            licenseText?.text = "Android Week View"
        }

        bt2I.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/alamkanak/Android-Week-View")))
        }
        bt2I.setOnLongClickListener {
            clipData = ClipData.newPlainText("", "https://github.com/alamkanak/Android-Week-View")
            clips?.primaryClip = clipData
            clipCopy("Android Week View")
            true
        }

        bt3.setOnClickListener {
            txtText.text = getString(R.string.ChatMessageView)
            licenseText?.text = "Chat Message View"
        }
        bt3I.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bassaer/ChatMessageView")))

        }
        bt3I.setOnLongClickListener {
            clipData = ClipData.newPlainText("", "https://github.com/bassaer/ChatMessageView")
            clips?.primaryClip = clipData
            clipCopy("Chat Message View")
            true
        }

        bt4.setOnClickListener {
            txtText.text = getString(R.string.AndroidImageCropper)
            licenseText?.text = "Android Image Cropper"
        }
        bt4I.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ArthurHub/Android-Image-Cropper")))

        }
        bt4I.setOnLongClickListener {
            clipData = ClipData.newPlainText("", "https://github.com/ArthurHub/Android-Image-Cropper")
            clips?.primaryClip = clipData
            clipCopy("Chat Message View")
            true
        }
    }


    private fun clipCopy(name: String?) {
        Toast.makeText(applicationContext, "클립보드에 복사했어! [$name]", Toast.LENGTH_SHORT).show()
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




