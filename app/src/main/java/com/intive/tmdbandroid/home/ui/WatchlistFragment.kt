package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentHomeBinding
import com.intive.tmdbandroid.home.ui.adapters.ScreeningPageAdapter
import com.intive.tmdbandroid.home.viewmodel.HomeViewModel
import com.intive.tmdbandroid.model.Screening
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import kotlin.math.floor

@AndroidEntryPoint
class WatchlistFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private val clickListener = { screening: Screening ->
//        val action = HomeFragmentDirections.actionHomeFragmentDestToDetailFragment(screening.id)
//        findNavController().navigate(action)
    }
    private val screeningPageAdapter = ScreeningPageAdapter(clickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.popularTVShows()
    }

    override fun onResume() {
        super.onResume()
        viewModel.watchlistScreening()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.title = getString(R.string.watchlist)

        initViews(binding)

        subscribePopularData(binding)
        subscribeWatchlistData(binding)

        return binding.root
    }

    private fun subscribePopularData(binding: FragmentHomeBinding) {
        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { resultTVShows ->
                Timber.i("MAS - popular tvshows status: $resultTVShows")

                when (resultTVShows) {
                    is State.Success<PagingData<Screening>> -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE

                        screeningPageAdapter.submitData(resultTVShows.data)

                        if (screeningPageAdapter.itemCount == 0) {
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
                    is State.Success<List<Screening>> -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        screeningPageAdapter.refreshWatchlistAdapter(it.data)
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
        val rvWatchlist = binding.rvWatchlist

        rvWatchlist.apply {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            Timber.i("MAS - dpWidth: $dpWidth")

            val scaling = resources.getInteger(R.integer.screening_width)
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

            screeningPageAdapter.widthSize = ((floor(dpWidth / columnCount) - resources.getInteger(R.integer.short_padding)) * displayMetrics.density).toInt()

            layoutManager = manager
            adapter = screeningPageAdapter
        }
    }
}
