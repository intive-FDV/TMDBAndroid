package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentHomeBinding
import com.intive.tmdbandroid.home.ui.adapters.WatchlistAdapter
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
        Toast.makeText(context, screening.name, Toast.LENGTH_SHORT).show()
    }
    private val watchlistAdapter = WatchlistAdapter(clickListener)

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

        subscribeWatchlistData(binding)

        return binding.root
    }

    private fun subscribeWatchlistData(binding: FragmentHomeBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.watchlistUIState.collectLatest {
                when(it) {
                    is State.Success<List<Screening>> -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE
                        watchlistAdapter.submitList(it.data)
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

            layoutManager = GridLayoutManager(context, columnCount)
            adapter = watchlistAdapter
        }
    }
}
