package com.pale_cosmos.helu


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.util.Util
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pale_cosmos.helu.util.myUtil
import kotlinx.android.synthetic.main.main_rv_item.view.*
import kotlinx.android.synthetic.main.main_rv_item.view.friendPhotoImg
import kotlinx.android.synthetic.main.main_rv_item.view.nameTv
import kotlinx.android.synthetic.main.sub_rv_item.view.*

class SubAdapter(items: ArrayList<ChatValue>, activity: Activity) : RecyclerView.Adapter<SubAdapter.SubViewHolder>() {

    var items = ArrayList<ChatValue>()
    var activity: Activity

    init {
        this.items = items
        this.activity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = SubViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        items[position].let { item ->
            with(holder) {
                //                if (item.photo != null) friendPhoto.setImageBitmap(myUtil.stringToBitmap(item.photo))
               var profile:Bitmap? = null
                when (item.key) {
                    myUtil.myKey -> {
                        profile = myUtil.stringToBitmap(item.yourprofile)
                        friendPhoto.setImageBitmap(profile)
                        friendName.text = item.yournickname
                    }
                    else -> {
                        profile=myUtil.stringToBitmap(item.profile)
                        friendPhoto.setImageBitmap(profile)
                        friendName.text = item.nickname

                    }


                }
                var clip = ""
                when (item.type) {
                    "message" -> {
                        if (item.message?.length!! > 10) clip = item.message?.substring(0, 10) + "..."
                        else clip = item.message!!
                    }
                    "photo" -> {
                        clip = "사진"
                    }

                }
                friendMessage.text = clip
                var information = myUtil.myInfo
                var image = myUtil.putDataHolder(profile)//니이미지집어넣기
//                var holder = myUtil.putDataHolder(information?.photo)//내프로필집어넣기
                myView.setOnClickListener {
                    var inf = Intent(activity,TalkActivity::class.java)
                    inf.putExtra("info", myUtil.putDataHolder(item))
                    inf.putExtra("nickname", information?.nickname)
                    inf.putExtra("key", myUtil.myKey)
                    inf.putExtra("image", image)
//                    inf.putExtra("profile", myUtil.putDataHolder(information?.photo))
                    activity.startActivityForResult(inf, 3) // 에러고치기

                }
            }
        }
    }

    fun deleteItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, items.size)
    }

    fun deleteAll() {
        items = ArrayList()
        notifyDataSetChanged()
    }

    inner class SubViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.sub_rv_item, parent, false)
    ) {
        val friendPhoto = itemView.friendPhotoImg2
        val friendName = itemView.nameTv!!
        val friendMessage = itemView.messageTv
        var myView = itemView

    }
}
