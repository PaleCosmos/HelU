package com.example.halilarm

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Handler
import android.support.design.widget.Snackbar
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




class LoginActivity : AppCompatActivity() {

    companion object {
        val dbName="Halilarm"
        val tableName="person"

        var personList:ArrayList<HashMap<String,String>>?=null
        var mHandler: Handler? = null
        var id_box: EditText? = null
        var pw_box: EditText? = null
        var logIn_button: Button? = null
        var register_button: TextView? = null
        var ester:ImageView?=null
        @JvmStatic
        var AC: Context? = null
        var returnB: Button? = null
        var auth: FirebaseAuth? = null
        var userModel: UserInfo? = null
        var database: FirebaseDatabase? = null
        var myRef: DatabaseReference? = null
        var nicks: String? = null
        var sh_Pref: SharedPreferences? = null
        var toEdit: SharedPreferences.Editor? = null
        var ch1: CheckBox? = null
        var ch2: CheckBox? = null
        var counter=0
    }

    var backKeyPressedTime: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
       // getWindow().setFlags(
         //   WindowManager.LayoutParams.FLAG_FULLSCREEN,
          //  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login)
        updateStatusBarColor("#E43F3F")
        //statusBarColorChanger()
        sh_Pref = getSharedPreferences("Login credentials", Context.MODE_PRIVATE)
        toEdit = sh_Pref?.edit()
        supportActionBar?.hide()
        AC = applicationContext
        ester=findViewById(R.id.ester)
        ester?.setOnClickListener {
            if(counter<5);
            else{
               makeCustomToast("이걸 왜 ${counter}번이나 눌렀니",0,-700)
            }
            counter++
        }
        auth = FirebaseAuth.getInstance()
        mHandler = Handler()
        id_box = findViewById(R.id.input_email)
        pw_box = findViewById(R.id.input_password)
        id_box?.setText("")
        pw_box?.setText("")
        logIn_button = findViewById(R.id.btn_login)
        ch1 = findViewById(R.id.saveID)
        ch1?.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                false -> {
                    ch2?.isChecked = false
                }
            }
        }
        ch2 = findViewById(R.id.loging)
        ch2?.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    ch1?.isChecked = true
                }
            }
        }
        returnB?.setOnClickListener {
            finish()
        }
        database = FirebaseDatabase.getInstance()

        register_button = findViewById(R.id.link_signup)

        logIn_button?.setOnClickListener {

            var id: String? = id_box?.text.toString().trim()
            var pass: String? = pw_box?.text.toString().trim()
            if (!(id.isNullOrBlank()) && !(pass.isNullOrBlank())) {
                logIn_button?.isEnabled = false
                id_box?.isEnabled = false
                pw_box?.isEnabled = false
                register_button?.isEnabled = false
                ch1?.isEnabled = false
                ch2?.isEnabled = false
                LoginCheck(id, pass)
                if (ch1?.isChecked!!) {

                    toEdit?.putBoolean("SAVEFLAG", true)
                    toEdit?.putString("Email", id)
                } else {
                    toEdit?.putBoolean("SAVEFLAG", false)
                }
                if (ch2?.isChecked!!) {
                    toEdit?.putBoolean("AUTOLOGIN", true)
                    toEdit?.putString("PASSWORD", pass)
                } else {
                    toEdit?.putBoolean("AUTOLOGIN", false)
                }
                toEdit?.commit()
            } else {
                logIn_button?.isEnabled = true
                id_box?.isEnabled = true
                pw_box?.isEnabled = true
                register_button?.isEnabled = true
                ch1?.isEnabled = true
                ch2?.isEnabled = true
                //Snackbar.make(applicationContext, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                makeCustomToast("정보를 확인해주세요.",0,500)
            }

        }

        register_button?.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)

            startActivity(intent)
        }
        applySharedPreference()
        /////////////////For Developers
        // DevelopMoreEasy()  // 출시때 빼야함@@@@@@@@@@@@@@@@@@@@
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000L) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(applicationContext, "One more you press back, EXIT", Toast.LENGTH_SHORT).show()
            return
        } else {
            setResult(RESULT_OK)
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
                        myRef = database?.getReference("users")?.child(uid)?.child("nickname")
                        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                nicks = p0.value.toString()
                                var intents = Intent(this@LoginActivity, MainActivity::class.java)
                                intents.putExtra("nickname", nicks)
                                startActivityForResult(intents, 101)

                                //Toast.makeText(this@LoginActivity,"nickname = $nicks",Toast.LENGTH_SHORT).show()
                            }

                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                        })

                    } else {
                        logIn_button?.isEnabled = true
                        id_box?.isEnabled = true
                        pw_box?.isEnabled = true
                        register_button?.isEnabled = true
                        ch1?.isEnabled = true
                        ch2?.isEnabled = true
                        //Toast.makeText(this@LoginActivity, "LogIn Error", Toast.LENGTH_SHORT).show()
                        makeCustomToast("로그인 실패",0,500)
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            logIn_button?.isEnabled = true
            id_box?.isEnabled = true
            pw_box?.isEnabled = true
            register_button?.isEnabled = true
            ch1?.isEnabled = true
            ch2?.isEnabled = true

            if (ch1?.isChecked == false) {
                id_box?.setText("")
            }
            if (ch2?.isChecked == false) {
                pw_box?.setText("")
            }
        }
        else if(resultCode == 1004){
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun makeCustomToast(msg: String, xSet:Int,ySet:Int) {
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

        Log.d("tq", "ddd")
        if (sh_Pref != null && sh_Pref!!.contains("Email") &&
            sh_Pref!!.getBoolean("SAVEFLAG", false)
        ) {
            var ML = sh_Pref?.getString("Email", "NULL")
            id_box?.setText(ML)
            ch1?.isChecked = true
            Log.d("SAVEFLAG", ML)
        }
        if (sh_Pref != null && sh_Pref!!.contains("Email") &&
            sh_Pref!!.getBoolean("AUTOLOGIN", false)
        ) {
            logIn_button?.isEnabled = false
            id_box?.isEnabled = false
            pw_box?.isEnabled = false
            register_button?.isEnabled = false
            ch1?.isEnabled = false
            ch2?.isEnabled = false
            var ML = sh_Pref?.getString("Email", "")
            id_box?.setText(ML)
            var PW = sh_Pref?.getString("PASSWORD", "")
            pw_box?.setText(PW)
            LoginCheck(ML!!, PW!!)
            ch2?.isChecked = true
            Log.d("AUTOLOGIN", PW)
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
