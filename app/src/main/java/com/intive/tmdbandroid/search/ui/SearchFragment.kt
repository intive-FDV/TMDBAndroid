package com.intive.tmdbandroid.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentSearchBinding
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.search.ui.adapters.TVShowSearchAdapter
import com.intive.tmdbandroid.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment: Fragment() {
    private val viewModel: SearchViewModel by viewModels()

    private val searchAdapter = TVShowSearchAdapter()

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.layoutProgressbar.progressBar.visibility = View.GONE
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()){
                    searchAdapter.query = query
                    binding.searchView.clearFocus()
                    viewModel.search(query)
                    subscribeViewModel()
                    initViews()
                    return true
                }
                return false
            }
        })
        binding.searchView.requestFocus()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onStart() {
        super.onStart()
        val imm = binding.searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        println(binding.searchView.isFocused)
        if(imm.isActive(binding.searchView) && binding.searchView.query.isEmpty()){
            imm.toggleSoftInput(0,0)
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.fragmentSearchToolbar
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    fun subscribeViewModel(){
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { resultTVShow ->

                when (resultTVShow) {
                    is State.Success<PagingData<TVShow>> -> {
                        binding.layoutSearchHint.hintContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        searchAdapter.submitData(resultTVShow.data)
                    }
                    is State.Error -> {
                        binding.layoutError.errorContainer.visibility = View.VISIBLE
                        binding.layoutSearchHint.hintContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                    }
                    is State.Loading -> {
                        binding.layoutSearchHint.hintContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initViews() {
        val resultsList = binding.searchResults

        resultsList.apply {
            searchAdapter.clickListener = { tvShow ->
                val action = SearchFragmentDirections.actionSearchFragmentToTVShowDetail(tvShow.id)
                findNavController().navigate(action)
            }

            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
    }
}