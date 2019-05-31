package com.pale_cosmos.helu

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import android.view.WindowManager
import android.os.Build
import com.pale_cosmos.helu.util.myUtil
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {


        var mHandler: Handler? = null
        var auth: FirebaseAuth? = null
        var userModel: UserInfo? = null
        var database: FirebaseDatabase? = null
        var myRef: DatabaseReference? = null
        var sh_Pref: SharedPreferences? = null
        var toEdit: SharedPreferences.Editor? = null
        var counter = 0


    var backKeyPressedTime: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        myUtil.updateStatusBarColor(window,"#E43F3F")
        sh_Pref = getSharedPreferences("Login credentials", Context.MODE_PRIVATE)
        toEdit = sh_Pref?.edit()
        supportActionBar?.hide()

        ester?.setOnClickListener {
            if (counter < 5) ;
            else {
                makeCustomToast("이걸 왜 ${counter}번이나 눌렀니", 0, -700)
            }
            counter++
        }
        auth = FirebaseAuth.getInstance()
        mHandler = Handler()
        input_email.setText("")
        input_password.setText("")

        saveID.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                false -> {
                    loging.isChecked = false
                }
            }
        }

        loging.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    saveID.isChecked = true
                }
            }
        }
        database = FirebaseDatabase.getInstance()



        btn_login.setOnClickListener {

            var id: String? = input_email.text.toString().trim()
            var pass: String? = input_password.text.toString().trim()
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
                input_email.error = "이메일 형식이 아닙니다."
                allEnabled()
            } else if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", pass)) {
                input_email.error = null
                input_password.error = "비밀번호는 8자 이상 20자 이하의 대소문자, 숫자, 특수문자로만 구성되어야 합니다."
                allEnabled()
            } else {
                allNotEnabled()
                input_email.error = null
                input_password.error = null
                LoginCheck(id!!, pass!!)

            }
        }

        link_signup.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)

            startActivity(intent)
        }
        applySharedPreference()

    }

    private fun allNotEnabled()=myUtil.logInDisable(btn_login,input_email,input_password,link_signup,saveID,loging)

    private fun allEnabled() =myUtil.logInEnable(btn_login,input_email,input_password,link_signup,saveID,loging)

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000L) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(applicationContext, "나갈거야?", Toast.LENGTH_SHORT).show()
            return
        } else {
            finish()
        }
    }

    fun LoginCheck(id: String, pw: String) {
        auth?.signInWithEmailAndPassword(id, pw)
            ?.addOnCompleteListener(this,
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        //var user = auth?.currentUser?.displayName
                        var uid = auth?.currentUser!!.uid
                        var myInfo: UserInfo?
//                    if(task.)



                        myRef = database?.getReference("users")?.child(uid)

                        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                myInfo = p0.getValue(UserInfo::class.java)

                                var intents = Intent(this@LoginActivity, MainActivity::class.java)

//                                intents.putExtra(myUtil.myUserInfo, myInfo)
                                var holderId = myUtil.putDataHolder(myInfo)

                                intents.putExtra(myUtil.myUserInfo, holderId)

                                intents.putExtra("key", uid)
                                if (saveID.isChecked) {

                                    toEdit?.putBoolean(myUtil.savLog, true)
                                    toEdit?.putString("Email", id)
                                } else {
                                    toEdit?.putBoolean(myUtil.savLog, false)
                                }
                                if (loging.isChecked) {
                                    toEdit?.putBoolean("AUTOLOGIN", true)
                                    toEdit?.putString("PASSWORD", pw)
                                } else {
                                    toEdit?.putBoolean("AUTOLOGIN", false)
                                }
                                toEdit?.commit()
                                startActivityForResult(intents, 101)
                                finish()

                            }

                            override fun onCancelled(p0: DatabaseError) {
                                 }
                        })

                    } else {
                        allEnabled()
                        input_password.error = "비밀번호가 올바르지 않습니다."
                        Log.d("gimma", task.toString())
                    }
                })
    }


    private fun makeCustomToast(msg: String, xSet: Int, ySet: Int) {
        var view: View =
            getLayoutInflater().inflate(R.layout.toastborder, findViewById<ViewGroup>(R.id.toast_layout_root))
        var text: TextView = view.findViewById(R.id.text)
        text.setTextColor(Color.WHITE)
        text.text = msg
        var toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER, xSet, ySet)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }

    private fun applySharedPreference() {

        if (sh_Pref != null && sh_Pref!!.contains("Email") &&
            sh_Pref!!.getBoolean(myUtil.savLog, false)
        ) {
            var ML = sh_Pref?.getString("Email", "NULL")
            input_email.setText(ML)
            saveID.isChecked = true

        }
        if (sh_Pref != null && sh_Pref!!.contains("Email") &&
            sh_Pref!!.getBoolean("AUTOLOGIN", false)
        ) {
            var ML = sh_Pref?.getString("Email", "")
            input_email.setText(ML)
            var PW = sh_Pref?.getString("PASSWORD", "")
            input_password.setText(PW)
            loging.isChecked = true

        }
    }

}
