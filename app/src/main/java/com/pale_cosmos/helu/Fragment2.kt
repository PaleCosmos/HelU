package com.pale_cosmos.helu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.pale_cosmos.helu.util.myUtil
import kotlinx.android.synthetic.main.license.view.*


class Fragment2 : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var named: TextView
    lateinit var university: TextView
    lateinit var department: TextView
    lateinit var phone: TextView
    lateinit var database: FirebaseDatabase
    lateinit var ref: DatabaseReference

    companion object {
        @JvmStatic
        lateinit var myAdapter: MainAdapter

        @JvmStatic
        var myList = arrayListOf<Friends>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment2, container, false) as ViewGroup
        recyclerView = view.findViewById(R.id.mRecyclerView)
        var holderId = arguments?.getString("myInfo")
        var info = myUtil.popDataHolder(holderId!!) as UserInfo
        database = FirebaseDatabase.getInstance()
        named = view.findViewById(R.id.nameTv)
        university = view.findViewById(R.id.universityTv)
        department = view.findViewById(R.id.departmentTv)
        phone = view.findViewById(R.id.phoneTv)
        ref = database.reference.child("users").child("${arguments?.getString("key")}").child("friends")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (x in p0.children) {
                    var v = x.getValue(Friends::class.java)
                    myList.add(v!!)
                }
                myAdapter = MainAdapter(myList)
                recyclerView.adapter = myAdapter
                recyclerView.layoutManager = LinearLayoutManager(view.context)
                phone.text = myUtil.phoneToString(info.phone!!)
                named.text = info.nickname
                university.text = info.university
                department.text = info.department
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })



        return view
    }


}