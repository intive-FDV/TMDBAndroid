package com.intive.tmdbandroid.search.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.FragmentSearchBinding
import com.intive.tmdbandroid.search.ui.adapters.TVShowSearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment: Fragment() {
//    private val viewModel: SearchViewModel by viewModels()

    private val searchAdapter = TVShowSearchAdapter()

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
//        viewModel.search()
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.fragmentSearchToolbar
        toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.searchView.requestFocus()
    }
}
