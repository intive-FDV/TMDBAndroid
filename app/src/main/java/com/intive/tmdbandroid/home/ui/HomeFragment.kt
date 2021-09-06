package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentHomeBinding
import com.intive.tmdbandroid.home.ui.adapters.TVShowPageAdapter
import com.intive.tmdbandroid.home.viewmodel.HomeViewModel
import com.intive.tmdbandroid.model.TVShow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private val tvShowPageAdapter = TVShowPageAdapter()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.popularTVShows()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initViews()
        subscribePopularData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.fragmentHomeToolbar
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.inflateMenu(R.menu.options_menu)
        toolbar.setOnMenuItemClickListener(){
            toolbar.findNavController().navigate(R.id.action_homeFragmentDest_to_searchFragment)
            true
        }
    }

    private fun subscribePopularData() {
        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { resultTVShows ->
                Log.i("MAS", "popular tvshows status: $resultTVShows")

                when (resultTVShows) {
                    is State.Success<PagingData<TVShow>> -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        tvShowPageAdapter.submitData(resultTVShows.data)

                        if (tvShowPageAdapter.itemCount == 0) {
                            binding.layoutEmpty.root.visibility = View.VISIBLE
                        } else binding.layoutEmpty.root.visibility = View.GONE
                    }
                    is State.Error -> {
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        binding.layoutError.errorContainer.visibility = View.VISIBLE
                    }
                    is State.Loading -> {
                        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
                        binding.layoutError.errorContainer.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initViews() {
        val rvTopTVShows = binding.rvPopularTVShows

        rvTopTVShows.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = tvShowPageAdapter
        }
    }
}
