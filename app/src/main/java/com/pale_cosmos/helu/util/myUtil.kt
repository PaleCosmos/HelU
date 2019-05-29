package com.pale_cosmos.helu.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.util.Base64
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.pale_cosmos.helu.GlideApp
import java.io.ByteArrayOutputStream

class myUtil {

    companion object {

        @JvmStatic
        val storageAddress = "gs://palecosmos-helu.appspot.com/"
        @JvmStatic
        val myUserInfo = "USERINFO"
        @JvmStatic
        val savLog = "SAVEFLAG"
        @JvmStatic
        val autoLog = "AUTOLOGIN"
        @JvmStatic
        val logIn_cred = "Login credentials"

        @JvmStatic
        fun stringToBitmap(encodedString: String): Bitmap {
            var encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            return image
        }

        @JvmStatic
        fun bitmapToString(encodedBitmap: Bitmap): String {
            var baos = ByteArrayOutputStream()
            encodedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            var bImage = baos.toByteArray()
            var base64 = Base64.encodeToString(bImage, Base64.DEFAULT)
            return base64
        }

        @JvmStatic
        fun buttonSetter(
            context: Context, bta1: Button, bta2: Button, txtText: TextView, licenseText: TextView,
            tx: String, lt: String, url: String
        ) {
            var clips = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            bta1.setOnClickListener {
                txtText.text = tx
                licenseText.text = lt

            }
            bta2.setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            bta2.setOnLongClickListener {
                var clipData = ClipData.newPlainText("", url)
                clips.primaryClip = clipData
                Toast.makeText(context, "클립보드에 복사했어! [$lt]", Toast.LENGTH_SHORT).show()
                true
            }

        }

        @JvmStatic
        fun codeSelect(activity: AppCompatActivity, num: Int, tv1: TextView, bt1: Button, bt2: Button) {
            var result = 0
            var CODE = -1


            when (num) {
                0 -> {
                    tv1.text = "U chat이 종료됩니다!"
                    bt1.text = "나가기"
                    bt2.text = "머무르기"
                    CODE = 0
                }
                1 -> {
                    tv1.text = "종료할까요?"
                    bt1.text = "네"
                    bt2.text = "아니요"
                    CODE = 1
                }
                2 -> {
                    tv1.text = "상대방이 나갔습니다!"
                    bt1.text = "친구추가"
                    bt2.text = "나가기"
                    CODE = 2
                }
                3->{

                    tv1.text = "상대방과 친구추가 할까요?"
                    bt1.text = "친구추가"
                    bt2.text = "아니요"
                    CODE = 3

                }
            }
            bt1.setOnClickListener {
                when (CODE) {
                    0 -> result = 88
                    1 -> result = 99
                    2 -> result = 10043
                    3-> {result = 7070}
                }
                activity.setResult(result)
                activity.finish()
            }
            bt2.setOnClickListener {
                if (CODE == 2) activity.setResult(10044)
                activity.finish()
            }


        }

        @JvmStatic
        fun logInEnable(a: Button, b: EditText, c: EditText, d: TextView, e: CheckBox, f: CheckBox) {
            a.isEnabled = true
            b.isEnabled = true
            c.isEnabled = true
            d.isEnabled = true
            e.isEnabled = true
            f.isEnabled = true
        }

        @JvmStatic
        fun logInDisable(a: Button, b: EditText, c: EditText, d: TextView, e: CheckBox, f: CheckBox) {
            a.isEnabled = false
            b.isEnabled = false
            c.isEnabled = false
            d.isEnabled = false
            e.isEnabled = false
            f.isEnabled = false
        }

        @JvmStatic
        fun updateStatusBarColor(window: Window, color: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.parseColor(color)
            }
        }

        @JvmStatic
        fun rotateTrue(
            applicationContext: Context,
            fab: FloatingActionButton,
            fab2: FloatingActionButton,
            fab3: FloatingActionButton,
            a: Int,
            b: Int
        ) {
            fab.startAnimation(AnimationUtils.loadAnimation(applicationContext, a))
            fab2.startAnimation(AnimationUtils.loadAnimation(applicationContext, b))
            fab3.startAnimation(AnimationUtils.loadAnimation(applicationContext, b))
            fab2.isClickable = false
            fab3.isClickable = false
        }

        @JvmStatic
        fun rotateFalse(
            applicationContext: Context,
            fab: FloatingActionButton,
            fab2: FloatingActionButton,
            fab3: FloatingActionButton,
            a: Int,
            b: Int
        ) {
            fab.startAnimation(AnimationUtils.loadAnimation(applicationContext, a))
            fab2.startAnimation(AnimationUtils.loadAnimation(applicationContext, b))
            fab3.startAnimation(AnimationUtils.loadAnimation(applicationContext, b))
            fab2.isClickable = true
            fab3.isClickable = true

        }

        @JvmStatic
        fun viberate(applicationContext: Context, mils: Long) {
            (applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(mils)
        }

        @JvmStatic
        fun initFragment(
            id: Int,
            supportFragmentManager: androidx.fragment.app.FragmentManager,
            frag2: androidx.fragment.app.Fragment,
            frag3: androidx.fragment.app.Fragment,
            frag4: androidx.fragment.app.Fragment
        ) {
            supportFragmentManager.beginTransaction().add(id, frag2).commit()
            supportFragmentManager.beginTransaction().add(id, frag3).commit()
            supportFragmentManager.beginTransaction().add(id, frag4).commit()
            supportFragmentManager.beginTransaction().hide(frag3).commit()
            supportFragmentManager.beginTransaction().hide(frag4).commit()
            supportFragmentManager.beginTransaction().show(frag2).commit()
        }

        @JvmStatic
        fun rotateFragment(
            id: Int,
            supportFragmentManager: androidx.fragment.app.FragmentManager,
            showFragment: androidx.fragment.app.Fragment,
            hideFragment1: androidx.fragment.app.Fragment,
            hideFragment2: androidx.fragment.app.Fragment
        ): Int {
            if (showFragment != null) {
                supportFragmentManager.beginTransaction().show(showFragment).commit()
            }
            if (hideFragment1 != null) {
                supportFragmentManager.beginTransaction().hide(hideFragment1).commit()
            }
            if (hideFragment2 != null) {
                supportFragmentManager.beginTransaction().hide(hideFragment2).commit()
            }

            return id
        }

        @JvmStatic
        fun errorDeleterInRegister(
            email: EditText,
            password1: EditText,
            password2: EditText,
            nickname: EditText,
            phone: EditText
        ) {
            email.error = null
            password1.error = null
            password2.error = null
            nickname.error = null
            phone.error = null
        }

        @JvmStatic
        fun errorMakerInRegister(
            email: EditText,
            password1: EditText,
            password2: EditText,
            nickname: EditText,
            phone: EditText,
            num: Int,
            msg: String
        ) {
            errorDeleterInRegister(email, password1, password2, nickname, phone)
            when (num) {
                0 -> email.error = msg
                1 -> password1.error = msg
                2 -> password2.error = msg
                3 -> nickname.error = msg
                4 -> phone.error = msg
            }
        }

        @JvmStatic
        fun registerEnable(
            a: EditText,
            b: EditText,
            c: EditText,
            d: EditText,
            e: AppCompatButton,
            f: TextView,
            g: RadioGroup,
            h: AppCompatSpinner,
            i: AppCompatSpinner,
            j: EditText
        ) {
            a.isEnabled = true
            b.isEnabled = true
            c.isEnabled = true
            d.isEnabled = true
            e.isEnabled = true
            f.isEnabled = true
            g.isEnabled = true
            h.isEnabled = true
            i.isEnabled = true
        }

        @JvmStatic
        fun registerDisable(
            a: EditText,
            b: EditText,
            c: EditText,
            d: EditText,
            e: AppCompatButton,
            f: TextView,
            g: RadioGroup,
            h: AppCompatSpinner,
            i: AppCompatSpinner,
            j: EditText
        ) {
            a.isEnabled = false
            b.isEnabled = false
            c.isEnabled = false
            d.isEnabled = false
            e.isEnabled = false
            f.isEnabled = false
            g.isEnabled = false
            h.isEnabled = false
            i.isEnabled = false

        }


    }
}