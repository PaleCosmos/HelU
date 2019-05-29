package com.pale_cosmos.helu

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.util.Util
import com.pale_cosmos.helu.util.myUtil
import kotlinx.android.synthetic.main.main_rv_item.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var items: MutableList<Friends> = mutableListOf(
        Friends("박상현", "r", "010-7677-7296", null, "가천대학교", "소프트웨어학과"))

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holder) {
                if (item.photo != null) friendPhoto.setImageBitmap(myUtil.stringToBitmap(item.photo))
                friendName.text = item.nickname
                friendUniv.text = item.university
                friendDepart.text = item.department
            }
        }
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.main_rv_item, parent, false)
    ) {
        val friendPhoto = itemView.friendPhotoImg
        val friendName = itemView.nameTv
        val friendUniv = itemView.universityTv
        val friendDepart = itemView.departmentTv
    }
}
