package com.example.halilarm
import android.content.Context

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {
    companion object {
        var databaseReference:DatabaseReference?=null
        var id_box: EditText? = null
        var pw_box: EditText? = null
        var pw_box2: EditText? = null
        var nick_box: EditText? = null
        var register_button: Button? = null
        var RTBT: TextView? = null

        @JvmStatic
        var apct: Context? = null
        var mHandler:Handler?=null


        var auth:FirebaseAuth?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_register)
        mHandler= Handler()
//        rd?.start()
        auth=FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        apct = applicationContext
        Log.d("th", "Get Register Activity")
        id_box = findViewById(R.id.email)
        pw_box = findViewById(R.id.password1)
        pw_box2 = findViewById(R.id.password2)
        nick_box = findViewById(R.id.nickname)
        RTBT = findViewById(R.id.link_return)
        register_button = findViewById(R.id.email_Register_button2)
        RTBT?.setOnClickListener {
            finish()
        }

        register_button?.setOnClickListener {
            val idInBox = id_box?.text.toString().trim()
            val passInBox = pw_box?.text.toString().trim()
            val passInBox2 = pw_box2?.text.toString().trim()
            val nickInBox = nick_box?.text.toString().trim()
           pw_box?.isEnabled=false
            pw_box2?.isEnabled=false
            nick_box?.isEnabled=false
            id_box?.isEnabled=false
            register_button?.isEnabled = false
            RTBT?.isEnabled=false
            if (idInBox.length < 3 || idInBox.length > 100) {
                pw_box?.setError(null)
                pw_box2?.setError(null)
                nick_box?.setError(null)
                id_box?.setError("ID must be in 4 to 10.")
            } else if (passInBox.length < 3 || passInBox.length > 15) {
                pw_box?.setError("Password must be 4 to 15")
                pw_box2?.setError(null)
                nick_box?.setError(null)
                id_box?.setError(null)
            } else if (!passInBox.equals(passInBox2)) {
                pw_box?.setError(null)
                pw_box2?.setError("Password is wrong.")
                nick_box?.setError(null)
                id_box?.setError(null)
            } else if (nickInBox.length < 2 || nickInBox.length > 8) {
                pw_box?.setError(null)
                pw_box2?.setError(null)
                nick_box?.setError("Nickname is must be 2 to 8")
                id_box?.setError(null)
            } else {
                auth?.createUserWithEmailAndPassword(idInBox, passInBox)
                    ?.addOnCompleteListener(this,
                        OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                var user = task.result?.user
                                var userModel = UserInfo()
                                userModel.email=user?.email
                                userModel.nickname=nickInBox
                                databaseReference?.child("users")?.child(user!!.uid)
                                    ?.setValue(userModel)
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity, "등록 에러", Toast.LENGTH_SHORT).show()
                                pw_box?.isEnabled=true
                                pw_box2?.isEnabled=true
                                nick_box?.isEnabled=true
                                id_box?.isEnabled=true
                                register_button?.isEnabled = true
                                RTBT?.isEnabled=true

                                return@OnCompleteListener
                            }
                        })
                    ?.addOnFailureListener { //task->

                    }


                register_button?.isEnabled = true
                RTBT?.isEnabled=true
                pw_box?.isEnabled=true
                pw_box2?.isEnabled=true
                nick_box?.isEnabled=true
                id_box?.isEnabled=true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("hala","Register terminated")
    }

    private fun makeCustomToast(msg: String) {
        var view: View =
            getLayoutInflater().inflate(R.layout.toastborder, findViewById<ViewGroup>(R.id.toast_layout_root))
        var text: TextView = view.findViewById(R.id.text)
        text.setTextColor(Color.WHITE)
        text.text = msg
        var toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER, 0, 500)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }



}