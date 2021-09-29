package com.intive.tmdbandroid.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentSearchBinding
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.search.ui.adapters.ScreeningSearchAdapter
import com.intive.tmdbandroid.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()
    private var isLoad:Boolean = false

    private lateinit var searchAdapter: ScreeningSearchAdapter

    private var searchViewQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clickListener = { screening: Screening ->
            val isMovie = screening.media_type == "movie"
            val action =
                SearchFragmentDirections.actionSearchFragmentToDetailFragment(screening.id, isMovie)
            val currentDestination = findNavController().currentDestination?.id
            if (currentDestination == R.id.searchFragmentDest) {
                findNavController().navigate(action)
            }
        }
        searchAdapter = ScreeningSearchAdapter(clickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {
            isLoad = savedInstanceState.getBoolean("isLoad", false)
        }
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupToolbar(binding)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    searchAdapter.query = query
                    binding.searchView.clearFocus()
                    viewModel.search(query)
                    searchViewQuery = query
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
        subscribeViewModel(binding)
        if (searchViewQuery.isEmpty() && !isLoad) {
            binding.searchView.requestFocus()
            val imm =
                binding.searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            binding.searchView.postDelayed({
                imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
            }, 50)
        } else {
            binding.layoutSearchHint.hintContainer.visibility = View.GONE
        }
        return binding.root
    }

    private fun setupToolbar(binding: FragmentSearchBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.fragmentSearchToolbar
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.navigationIcon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { requireActivity().finish() }
    }

    private fun subscribeViewModel(binding: FragmentSearchBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { screening ->
                when (screening) {
                    is State.Success<PagingData<Screening>> -> {
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutEmpty.root.visibility = View.GONE
                        binding.layoutSearchHint.hintContainer.visibility = View.GONE
                        searchAdapter.notifyItemChanged(0)
                        searchAdapter.submitData(screening.data)
                    }
                    is State.Error -> {
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        binding.layoutEmpty.root.visibility = View.GONE
                        binding.layoutSearchHint.hintContainer.visibility = View.GONE
                        binding.layoutError.errorContainer.visibility = View.VISIBLE
                    }
                    is State.Loading -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutEmpty.root.visibility = View.GONE
                        binding.layoutSearchHint.hintContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
                    }
                    is State.Waiting -> {
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutEmpty.root.visibility = View.GONE
                        binding.layoutSearchHint.hintContainer.visibility = View.VISIBLE
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
        lifecycleScope.launchWhenStarted {
            searchAdapter.differ.loadStateFlow.collectLatest { loadState ->
                if (loadState.refresh is LoadState.NotLoading &&
                    loadState.append is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached
                ) {
                    binding.layoutEmpty.root.isVisible = searchAdapter.itemCount < 2
                }
            }
        }

    }
}