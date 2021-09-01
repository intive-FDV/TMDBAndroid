package com.intive.tmdbandroid.details.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.intive.tmdbandroid.R

class TVShowDetail : Fragment() {
    private var tvShowId : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tvShowId = it.getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tv_show_detail, container, false)

        val tvShowDetailText = view.findViewById<TextView>(R.id.tvShowDetailText)
        tvShowDetailText.text = tvShowId.toString()

        return view
    }
}