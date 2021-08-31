package com.intive.tmdbandroid.common.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intive.tmdbandroid.databinding.FragmentEmptyBinding

class EmptyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEmptyBinding.inflate(inflater, container, false)
        return binding.root
    }
}