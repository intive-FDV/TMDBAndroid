package com.intive.tmdbandroid.details.ui.person.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.intive.tmdbandroid.databinding.FragmentDetailPersonBinding
import com.intive.tmdbandroid.details.ui.person.viewmodel.DetailPersonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPersonFragment: Fragment() {
    private val viewModel: DetailPersonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailPersonBinding.inflate(inflater, container, false)
        collectCombineCredits()
        collectDetailPerson()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun collectCombineCredits() {
        TODO("Not yet implemented")
    }

    private fun collectDetailPerson() {
        TODO("Not yet implemented")
    }

}