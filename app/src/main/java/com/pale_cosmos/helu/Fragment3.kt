package com.pale_cosmos.helu

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
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


class Fragment3 : Fragment() {
    lateinit var recyclerView: RecyclerView

    lateinit var database: FirebaseDatabase
    lateinit var ref: DatabaseReference

    companion object {
        //@SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var myAdapter: SubAdapter

        @JvmStatic
        lateinit var myList: ArrayList<ChatValue>
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment3, container, false) as ViewGroup
        recyclerView = view.findViewById(R.id.mRecyclerView2)
        myList = ArrayList()
        database = FirebaseDatabase.getInstance()
        ref = database.reference.child("users").child("${arguments?.getString("key")}").child("talk")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("mytag", p0.key)
                for (x in p0.children) {
                    Log.d("mytag0", x.key)
                    var temrf = x.ref.orderByKey().limitToLast(1)

                    temrf.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            Log.d("mytag1", p0.key)
                            for (data in p0.children) {
                                val log = data.getValue(ChatValue::class.java)
                                Log.d("mytag2", data.key)
                                Log.d("mytag3", log?.key)
                                myList.add(log!!)
                            }
                        }

                    })
                }
                myAdapter = SubAdapter(myList, activity!!)
                recyclerView.adapter = myAdapter
                recyclerView.layoutManager = LinearLayoutManager(view.context)
                myAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })



        return view
    }


}