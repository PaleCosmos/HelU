package com.pale_cosmos.helu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Fragment2 : Fragment() {
    lateinit var recyclerView:RecyclerView
var myList = arrayListOf<Friends>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment2, container, false) as ViewGroup
        recyclerView=view.findViewById(R.id.mRecyclerView)
        recyclerView.adapter=MainAdapter()
        recyclerView.layoutManager=LinearLayoutManager(view.context)
        return view
    }
}