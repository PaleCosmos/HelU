package com.pale_cosmos.helu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Fragment2 : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var named:TextView
    lateinit var university:TextView
    lateinit var department:TextView
    lateinit var myAdapter: MainAdapter
var myList = arrayListOf<Friends>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment2, container, false) as ViewGroup
        recyclerView=view.findViewById(R.id.mRecyclerView)
        recyclerView.adapter=MainAdapter()

        recyclerView.layoutManager=LinearLayoutManager(view.context)
        myAdapter= MainAdapter()
//https://androidyongyong.tistory.com/5

        named = view.findViewById(R.id.nameTv)
        university=view.findViewById(R.id.universityTv)
        department=view.findViewById(R.id.departmentTv)

        return view
    }
    fun setTextChange(name:String?,univ:String?,depa:String?)
    {
            named.text=name
        university.text=univ
        department.text=depa
    }
}