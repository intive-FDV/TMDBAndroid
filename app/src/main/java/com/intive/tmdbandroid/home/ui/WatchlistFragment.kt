package com.intive.tmdbandroid.home.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentHomeBinding
import com.intive.tmdbandroid.detailandsearch.ui.DetailAndSearchActivity
import com.intive.tmdbandroid.home.ui.adapters.WatchlistAdapter
import com.intive.tmdbandroid.home.viewmodel.WatchlistViewModel
import com.intive.tmdbandroid.model.Screening
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.floor

@AndroidEntryPoint
class WatchlistFragment : Fragment() {
    private val viewModel: WatchlistViewModel by navGraphViewModels(R.id.bottom_nav_graph) {
        defaultViewModelProviderFactory
    }

    private val clickListener = { screening: Screening ->
        val intent = Intent(requireActivity(), DetailAndSearchActivity::class.java)
        val isMovie = screening.media_type == "movie"
        intent.putExtras(
            bundleOf(
                "action" to "detail",
                "screeningID" to screening.id,
                "isMovieBoolean" to isMovie
            )
        )
        requireActivity().startActivity(intent)
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

        initViews(binding)

        subscribeWatchlistData(binding)

        return binding.root
    }

    private fun subscribeWatchlistData(binding: FragmentHomeBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
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

            val scaling = resources.getInteger(R.integer.screening_width)
            val columnCount = floor(dpWidth / scaling).toInt()

            layoutManager = GridLayoutManager(context, columnCount)
            adapter = watchlistAdapter
        }
    }
}