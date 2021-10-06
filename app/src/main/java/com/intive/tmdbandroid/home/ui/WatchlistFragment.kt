package com.intive.tmdbandroid.home.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentWatchlistBinding
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

    private lateinit var watchlistAdapter: WatchlistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clickListener = { screening: Screening ->
            val intent = Intent(requireActivity(), DetailAndSearchActivity::class.java)
            intent.putExtras(
                bundleOf(
                    (context?.getString(R.string.intent_extra_key_action) ?: "") to (context?.getString(R.string.intent_extra_key_action_detail) ?: ""),
                    (context?.getString(R.string.intent_extra_key_screening_id) ?: "") to screening.id,
                    (context?.getString(R.string.intent_extra_key_is_movie) ?: "") to (screening.media_type == context?.getString(R.string.screening_movie_type))
                )
            )
            requireActivity().startActivity(intent)
        }
        watchlistAdapter = WatchlistAdapter(clickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initViews(binding)

        subscribeWatchlistData(binding)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.watchlistScreening()
    }

    private fun subscribeWatchlistData(binding: FragmentWatchlistBinding) {
        lifecycleScope.launchWhenResumed {
            viewModel.uiState.collectLatest {
                when(it) {
                    is State.Success<List<Screening>> -> {
                        binding.layoutError.root.visibility = View.GONE
                        binding.layoutProgressbar.root.visibility = View.GONE

                        binding.layoutNodata.root.isVisible = it.data.isEmpty()

                        watchlistAdapter.submitList(it.data)
                    }
                    is State.Error -> {
                        binding.layoutProgressbar.root.visibility = View.GONE
                        binding.layoutError.root.visibility = View.VISIBLE
                    }
                    is State.Loading -> {
                        binding.layoutProgressbar.root.visibility = View.VISIBLE
                        binding.layoutError.root.visibility = View.GONE
                    }
                    else -> throw RuntimeException(context?.getString(R.string.state_error))
                }
            }
        }
    }

    private fun initViews(binding: FragmentWatchlistBinding) {
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
