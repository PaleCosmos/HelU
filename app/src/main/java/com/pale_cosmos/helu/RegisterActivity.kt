package com.pale_cosmos.helu

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter.*
import androidx.core.content.FileProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    var databaseReference: DatabaseReference? = null
    var mHandler: Handler? = null
    var auth: FirebaseAuth? = null
    var choice_univ: String? = null
    var choice_depart: String? = null
    lateinit var spinCheck_univ: ArrayAdapter<CharSequence>
    lateinit var spinCheck_depart: ArrayAdapter<CharSequence>
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var authReference: StorageReference
    lateinit var uidReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_register)
        mHandler = Handler()
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        mancheck.isChecked = true
        link_return.setOnClickListener {
            finish()
        }
        storage = FirebaseStorage.getInstance("gs://palecosmos-helu.appspot.com/")
        storageReference = storage.reference

        email_Register_button2.setOnClickListener {
            validChecker()
        }
        spinCheck_univ = createFromResource(
            applicationContext, R.array.spinner_univ,
            R.layout.spinner_item
        )
        spinCheck_univ.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner_univ.adapter = spinCheck_univ

        spinner_univ.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spinCheck_univ.getItem(position).toString().equals("가천대학교", ignoreCase = true)) {
                    choice_univ = "가천대학교"
                    spinCheck_depart = createFromResource(applicationContext, R.array.spinner_dm, R.layout.spinner_item)

                    spinCheck_depart.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    spinner_depart.adapter = spinCheck_depart
                    spinner_depart.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            choice_depart = spinCheck_depart.getItem(position).toString()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
                }
            }
        }
    }

    private fun errorDeleter() {
        email.error = null
        password1.error = null
        password2.error = null
        nickname.error = null
        phone.error = null
    }

    private fun errorMaker(num: Int, msg: String) {
        errorDeleter()
        when (num) {
            0 -> email.error = msg
            1 -> password1.error = msg
            2 -> password2.error = msg
            3 -> nickname.error = msg
            4 -> phone.error = msg
        }
    }

    private fun validChecker() {
        val idInBox = email.text.toString().trim()
        val passInBox = password1.text.toString().trim()
        val passInBox2 = password2?.text.toString().trim()
        val nickInBox = nickname.text.toString().trim()
        var gender: Boolean = (mancheck.isChecked)
        var phones = phone.text.toString().trim()
        allNotEnabled()

        // 0 -> Email
        // 1 -> passWord1
        // 2 -> passWord2
        // 3 -> nickname
        // 4 ->Phone
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(idInBox).matches()) {
            errorMaker(0, "이메일 형식이 아닙니다.")
            allEnabled()
        } else if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", passInBox)) {
            errorMaker(1, "비밀번호는 8자 이상 20자 이하의 대소문자, 숫자, 특수문자로만 구성되어야 합니다.")
            allEnabled()
        } else if (!passInBox.equals(passInBox2)) {
            errorMaker(2, "비밀번호가 올바르지 않습니다.")
            allEnabled()
        } else if (nickInBox.length < 2 || nickInBox.length > 8) {
            errorMaker(3, "닉네임은 2자 이상 8자 이하로 구성되어야 합니다.")
            allEnabled()
        } else if (!android.util.Patterns.PHONE.matcher(phones).matches()) {
            errorMaker(4, "번호의 형식이 올바르지않습니다.")
            allEnabled()
        } else {
            auth?.createUserWithEmailAndPassword(idInBox, passInBox)
                ?.addOnCompleteListener(this,
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            var user = task.result?.user
                            var userModel = UserInfo()
                            userModel.email = user?.email
                            userModel.nickname = nickInBox
                            userModel.gender = gender
                            userModel.university = choice_univ
                            userModel.department = choice_depart
                            userModel.phone = phones
                            databaseReference?.child("users")?.child(user!!.uid)
                                ?.setValue(userModel)
                            // JPG 파일인지 확인 필요
                            authReference = storageReference.child("profile")
                            uidReference = authReference.child("${task.result?.user?.uid}.png")

                            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.profile)
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                            var uploadTask = uidReference.putBytes(baos.toByteArray())
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "등록 에러", Toast.LENGTH_SHORT).show()


                            return@OnCompleteListener
                        }
                    })
                ?.addOnFailureListener {

                    Toast.makeText(this@RegisterActivity, "등록 에러", Toast.LENGTH_SHORT).show()
                    allEnabled()
                }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("hala", "Register terminated")
    }

    private fun allNotEnabled() {
        password1.isEnabled = false
        password2?.isEnabled = false
        nickname.isEnabled = false
        email.isEnabled = false
        email_Register_button2.isEnabled = false
        link_return.isEnabled = false
        genderChecker.isEnabled = false
        spinner_depart.isEnabled = false
        spinner_univ.isEnabled = false
        phone.isEnabled = false
    }

    private fun allEnabled() {
        password1.isEnabled = true
        password2?.isEnabled = true
        nickname.isEnabled = true
        email.isEnabled = true
        email_Register_button2.isEnabled = true
        link_return.isEnabled = true
        genderChecker.isEnabled = true
        spinner_depart.isEnabled = true
        spinner_univ.isEnabled = true
        phone.isEnabled = true
    }

    private fun makeCustomToast(msg: String) {
        var view: View =
            layoutInflater.inflate(R.layout.toastborder, findViewById<ViewGroup>(R.id.toast_layout_root))
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