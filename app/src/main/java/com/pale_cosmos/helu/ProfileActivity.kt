package com.pale_cosmos.helu

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    val PICK_FROM_CAMERA = 0
    val PICK_FROM_ALBUM = 1

    lateinit var tempFile: File
    private var isCamera = false
    lateinit var photoUri: Uri
    lateinit var imageCaptureUrl: Uri
    var integerByResult = -1  // 1이면 프사변경 2면 사진 전송

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_profile)
        integerByResult = intent.getIntExtra("by", -1)
        Cameras.setOnClickListener(this)
        Albums.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.Cameras) getImageFromCamera()
        else if (v?.id == R.id.Albums) getImageFromAlbum()
    }


    private fun getImageFromAlbum() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {


                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(applicationContext, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionGranted() {
                    tempFile = createImageFile()
                    isCamera = false
                    var pic = Intent(Intent.ACTION_GET_CONTENT)
                    pic.type = "image/*"
                    startActivityForResult(Intent.createChooser(pic, "Get Album"), PICK_FROM_ALBUM)
                }
            })
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) // 확인할 권한을 다중 인자로 넣어줍니다.
            .check()
    }

    private fun getImageFromCamera() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(applicationContext, "권한이 없습니다.", Toast.LENGTH_SHORT).show()

                }

                override fun onPermissionGranted() {
                    tempFile = createImageFile()
                    isCamera = true
                    var pic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    try {
                        tempFile = createImageFile()
                    } catch (e: IOException) {
                        Toast.makeText(applicationContext, "이미지 처리 오류", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        photoUri = FileProvider.getUriForFile(
                            this@ProfileActivity,
                            BuildConfig.APPLICATION_ID + ".provider",
                            tempFile
                        )
                        Log.d("whatFile", BuildConfig.APPLICATION_ID + ".provider")


                    } else {
                        photoUri = Uri.fromFile(tempFile)

                    }
                    imageCaptureUrl = photoUri

                    pic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(pic, PICK_FROM_CAMERA)
                }
            })
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) // 확인할 권한을 다중 인자로 넣어줍니다.
            .check()
    }

    private fun createImageFile(): File {
        var timeStamp = SimpleDateFormat("HHmmss").format(Date())
        var ImageFileName = "bumbaya" + timeStamp + "_"
        var storageDir = File(Environment.getExternalStorageDirectory(), "/test/")
        if (!storageDir.exists()) storageDir.mkdirs()
        var image = File.createTempFile(ImageFileName, ".jpg", storageDir)
        return image
    }

    private fun startCroping(fileUri: Uri) {
        CropImage.activity(fileUri)
            .setActivityMenuIconColor(Color.WHITE)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setBorderLineColor(Color.parseColor("#CC1D1D"))
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var tenty = Intent()
        if (resultCode != RESULT_OK) {
            finish()
        }
        when (requestCode) {
            PICK_FROM_ALBUM -> {

                var fileUri = data?.data

                startCroping(fileUri!!)

            }
            PICK_FROM_CAMERA -> {
                if (data?.data == null) {
                    startCroping(imageCaptureUrl)

                } else {

                    startCroping(data.data)
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {

                var result = CropImage.getActivityResult(data)
                var resultUri = result.uri

                tenty.putExtra("profileUri", resultUri)

                setResult(75, tenty)
                finish()

            }
        }
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }

    }
}