package com.intive.tmdbandroid.home.ui

import android.os.Bundle
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
import timber.log.Timber
import kotlin.math.floor

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var tvShowPageAdapter: TVShowPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.popularTVShows()
    }

    override fun onResume() {
        super.onResume()
        viewModel.watchlistTVShows()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initViews(binding)
        subscribePopularData(binding)
        setupToolbar(binding)
        subscribeWatchlistData(binding)
        return binding.root
    }

    private fun setupToolbar(binding: FragmentHomeBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.fragmentHomeToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.fragmentHomeToolbar.inflateMenu(R.menu.options_menu)
        binding.fragmentHomeToolbar.setOnMenuItemClickListener{
            binding.fragmentHomeToolbar.findNavController().navigate(R.id.action_homeFragmentDest_to_searchFragment)
            true
        }
    }

    private fun subscribePopularData(binding: FragmentHomeBinding) {
        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { resultTVShows ->
                Timber.i("MAS - popular tvshows status: $resultTVShows")

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

    private fun subscribeWatchlistData(binding: FragmentHomeBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.watchlistUIState.collectLatest {
                when(it) {
                    is State.Success<List<TVShow>> -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        tvShowPageAdapter.refreshWatchlistAdapter(it.data)
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

    private fun initViews(binding: FragmentHomeBinding) {
        val rvTopTVShows = binding.rvPopularTVShows

        val clickListener = { tvShow: TVShow ->
            val action = HomeFragmentDirections.actionHomeFragmentDestToTVShowDetail(tvShow.id)
            findNavController().navigate(action)
        }
        tvShowPageAdapter = TVShowPageAdapter(clickListener)

        rvTopTVShows.apply {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density

            val scaling = 200
            val columnCount = floor(dpWidth / scaling).toInt()
            Timber.i("MAS - columnCount: $columnCount")

            val manager = GridLayoutManager(context, columnCount)
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0, 1, 2 -> columnCount
                        else -> 1
                    }
                }
            }

            layoutManager = manager
            adapter = tvShowPageAdapter
        }
    }
}
