package com.pale_cosmos.helu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import com.google.android.gms.common.data.DataHolder
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pale_cosmos.helu.util.myUtil

class SplashActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    var sh_Pref: SharedPreferences? = null
    var toEdit: SharedPreferences.Editor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        myUtil.updateStatusBarColor(window, "#E43F3F")
        setContentView(R.layout.splash)

        sh_Pref = getSharedPreferences(myUtil.logIn_cred, Context.MODE_PRIVATE)
        toEdit = sh_Pref?.edit()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        ContentReceive().execute("")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()


        }
    }


    inner class ContentReceive : AsyncTask<String, Void, String>() {


        override fun doInBackground(vararg params: String?): String {
            var resulting = "waiting"

            if (sh_Pref != null && sh_Pref!!.contains("Email") &&
                sh_Pref!!.getBoolean(myUtil.savLog, false) &&
                sh_Pref!!.getBoolean(myUtil.autoLog, false) &&
                sh_Pref!!.contains("PASSWORD")
            ) {

                val id = sh_Pref?.getString("Email", "NULL")
                val pass = sh_Pref?.getString("PASSWORD", "NULL")
                auth.signInWithEmailAndPassword(id!!, pass!!)
                    .addOnCompleteListener(this@SplashActivity,
                        OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                resulting = "success"

                            } else {
                                resulting = "failed"

                            }
                        })

                while (resulting == "waiting") {
                    Thread.sleep(50)
                }
            }

            return resulting

        }

        override fun onPostExecute(result: String?) {
            when (result) {
                "success" -> {

                    //var user = auth?.currentUser?.displayName
                    var uid = auth?.currentUser!!.uid
                    var myInfo: UserInfo?
                    myRef = database?.getReference("users")?.child(uid)

                    myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            myInfo = p0.getValue(UserInfo::class.java)

                            var intents = Intent(this@SplashActivity, MainActivity::class.java)

                            intents.putExtra("key", uid)
                            var holderId = myUtil.putDataHolder(myInfo)

                            intents.putExtra(myUtil.myUserInfo, holderId)

                            intents.putExtra("university", myInfo?.university)
                            intents.putExtra("department", myInfo?.department)


                            startActivity(intents)//메인으로바로넘어감
                            finish()

                        }

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
                "failed" -> {
                    Handler().postDelayed(DelayedSplash(), 800)
                }
                "waiting" -> {
                    Handler().postDelayed(DelayedSplash(), 800)
                }
            }
        }
    }

    inner class DelayedSplash : Thread() {
        override fun run() {

            var intents = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intents) //로그인액티비티
            finish()

        }
    }
}