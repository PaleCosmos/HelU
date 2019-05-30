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
import com.pale_cosmos.helu.util.myUtil
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
//    lateinit var storage: FirebaseStorage
//    lateinit var storageReference: StorageReference
//    lateinit var authReference: StorageReference
//    lateinit var uidReference: StorageReference

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
//        storage = FirebaseStorage.getInstance("gs://palecosmos-helu.appspot.com/")
//        storageReference = storage.reference

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

    private fun errorMaker(num: Int, msg: String)  = myUtil.errorMakerInRegister(email,password1,password2,nickname,phone,num,msg)

    private fun validChecker() {
        val idInBox = email.text.toString().trim()
        val passInBox = password1.text.toString().trim()
        val passInBox2 = password2?.text.toString().trim()
        val nickInBox = nickname.text.toString().trim()
        var gender: Boolean = (mancheck.isChecked)
        var phones = phone.text.toString().trim()
        allNotEnabled()

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
        } else if (!myUtil.phoneValid(phones)) {
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
                            userModel.photo = myUtil.bitmapToString(BitmapFactory.decodeResource(resources, R.drawable.profile))

                            databaseReference?.child("users")?.child(user!!.uid)
                                ?.setValue(userModel)
                            var myfriend = Friends()
                            myfriend.setValue(
                                nickInBox,
                                user!!.uid,
                                phones,
                                "0",
                                choice_univ!!,
                                choice_depart!!
                            )

                            // JPG 파일인지 확인 필요


                            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.profile)
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                            FirebaseStorage.getInstance().reference.child("profile").child("${task.result?.user?.uid}.png").putBytes(baos.toByteArray())

                            var mm = Friends()
                            mm.setValue("박상현","p26hrHybdpZIv3glbpCWu8jHkYo1","01076777296","0","가천대학교",
                                "소프트웨어학과")
                            databaseReference?.child("users")?.child(user!!.uid)?.child("friends")
                                ?.child("p26hrHybdpZIv3glbpCWu8jHkYo1")?.setValue(mm)
                            databaseReference?.child("users")?.child("p26hrHybdpZIv3glbpCWu8jHkYo1")?.child("friends")
                                ?.child(user?.uid)?.setValue(myfriend)
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

    private fun allNotEnabled() =
        myUtil.registerDisable(password1,password2,nickname,email,email_Register_button2,link_return,genderChecker,spinner_depart,spinner_univ,phone)


    private fun allEnabled() =
        myUtil.registerEnable(password1,password2,nickname,email,email_Register_button2,link_return,genderChecker,spinner_depart,spinner_univ,phone)


}