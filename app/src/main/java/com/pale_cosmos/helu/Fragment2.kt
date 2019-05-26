package com.pale_cosmos.helu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class Fragment2 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment2, container, false) as ViewGroup
        var bt = view.findViewById<Button>(R.id.stdstd)
        bt.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("way_w3w://start/529670")))

        }
        return view
    }
}