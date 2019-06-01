package com.pale_cosmos.helu


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.*
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

    inner class SubViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.sub_rv_item, parent, false)
    ), View.OnCreateContextMenuListener {
        val friendPhoto = itemView.friendPhotoImg2
        val friendName = itemView.nameTv!!
        val friendMessage = itemView.messageTv
        var myView = itemView
        var onEditMenu = MenuItem.OnMenuItemClickListener {
            if (it.itemId == 1001) {
                FirebaseDatabase.getInstance().reference.child("users").child(myUtil.myKey)
                    .child("talk").child(items.get(adapterPosition).key!!).removeValue()
                items.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition, items.size)

            }
            true
        }

        init {
            myView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            var delete = menu?.add(Menu.NONE, 1001, 1, "삭제")
            delete?.setOnMenuItemClickListener(onEditMenu)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = SubViewHolder(parent)


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        items[position].let { item ->
            with(holder) {
                //                if (item.photo != null) friendPhoto.setImageBitmap(myUtil.stringToBitmap(item.photo))
                var profile: Bitmap? = null
                when (item.key) {
                    myUtil.myKey -> {
                        profile = myUtil.stringToBitmap(item.yourprofile)
                        friendPhoto.setImageBitmap(profile)
                        friendName.text = item.yournickname
                    }
                    else -> {
                        profile = myUtil.stringToBitmap(item.profile)
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
                var image = myUtil.putDataHolder(item.yourprofile)//니이미지집어넣기
//                var holder = myUtil.putDataHolder(information?.photo)//내프로필집어넣기
                myView.setOnCreateContextMenuListener { menu, v, menuInfo ->
                    var delete = menu.add(Menu.NONE, 1001, 1, "삭제")
                    delete.setOnMenuItemClickListener(onEditMenu)
                }
                myView.setOnClickListener {
                    var inf = Intent(activity, TalkActivity::class.java)
                    inf.putExtra("info", myUtil.putDataHolder(typeCasting(item)))
                    inf.putExtra("nickname", information?.nickname)
                    inf.putExtra("key", myUtil.myKey)
                    inf.putExtra("image", image)
//                    inf.putExtra("profile", myUtil.putDataHolder(information?.photo))
                    activity.startActivityForResult(inf, 3) // 에러고치기

                }
            }
        }
    }

    fun typeCasting(chatValue: ChatValue): Friends {
        var friends = Friends()
        if (myUtil.myKey == chatValue.key) {
            friends.setValue(
                chatValue.yournickname,
                chatValue.yourkey,
                chatValue.yourprofile, "", "", "", ""
            )

        } else {
            friends.setValue(
                chatValue.nickname!!,
                chatValue.key!!,
                chatValue.profile, "", "", "", ""
            )
        }

        return friends
    }

    fun deleteItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    fun deleteAll() {
        items = ArrayList()
        notifyDataSetChanged()
    }


}
