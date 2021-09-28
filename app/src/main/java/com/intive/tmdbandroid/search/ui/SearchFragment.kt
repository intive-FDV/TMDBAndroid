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
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentSearchBinding
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.search.ui.adapters.ScreeningSearchAdapter
import com.intive.tmdbandroid.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment: Fragment() {
    private val viewModel: SearchViewModel by viewModels()
    private var isLoad:Boolean = false;

    private val clickListener = { screening: Screening ->
        val isMovie = screening.media_type == "movie"
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(screening.id, isMovie)
        findNavController().navigate(action)
    }

    private val searchAdapter = ScreeningSearchAdapter(clickListener)

    private var searchViewQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {
            isLoad = savedInstanceState.getBoolean("isLoad", false)
        }
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.layoutProgressbar.progressBar.visibility = View.GONE
        setupToolbar(binding)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()){
                    searchAdapter.query = query
                    binding.searchView.clearFocus()
                    viewModel.search(query)
                    searchViewQuery = query
                    subscribeViewModel(binding)
                    isLoad=true
                    return true
                }
                return false
            }
        })
        if(savedInstanceState!=null && isLoad){
            binding.searchView.clearFocus()
            subscribeViewModel(binding)
        }
        initViews(binding)
        if(searchViewQuery.isEmpty() && !isLoad){
            binding.searchView.requestFocus()
            val imm = binding.searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            binding.searchView.postDelayed(  {
                imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
            }, 50)
        }
        else{
            binding.layoutSearchHint.hintContainer.visibility = View.GONE
        }
        return binding.root
    }

    private fun setupToolbar(binding: FragmentSearchBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.fragmentSearchToolbar
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    fun subscribeViewModel(binding: FragmentSearchBinding){
        searchAdapter.notifyItemChanged(0)
        searchAdapter.differ.addLoadStateListener { loadState ->
            if(loadState.append.endOfPaginationReached){
                if (searchAdapter.itemCount < 1 + 1) {
                    binding.layoutEmpty.root.visibility = View.VISIBLE
                } else binding.layoutEmpty.root.visibility = View.GONE
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { screening ->

                when (screening) {
                    is State.Success<PagingData<Screening>> -> {
                        binding.layoutSearchHint.hintContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        searchAdapter.submitData(screening.data)
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

    private fun initViews(binding: FragmentSearchBinding) {
        val resultsList = binding.searchResults

        resultsList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
    }
}