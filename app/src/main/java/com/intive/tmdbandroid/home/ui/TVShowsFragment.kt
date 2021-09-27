package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentTvshowsBinding
import com.intive.tmdbandroid.home.ui.adapters.ScreeningPageAdapter
import com.intive.tmdbandroid.home.viewmodel.TVShowsViewModel
import com.intive.tmdbandroid.model.Screening
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import kotlin.math.floor

@AndroidEntryPoint
class TVShowsFragment : Fragment() {
    private val viewModel: TVShowsViewModel by navGraphViewModels(R.id.bottom_nav_graph) {
        defaultViewModelProviderFactory
    }

    private val clickListener = { screening: Screening ->
//        val action = WatchlistFragmentDirections.actionHomeFragmentDestToTVShowDetail(tvShow.id)
//        findNavController().navigate(action)
        Toast.makeText(context, screening.name, Toast.LENGTH_SHORT).show()
    }
    private val tvShowPageAdapter = ScreeningPageAdapter(clickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            Timber.i("MAS - tvshows instance = null")
            viewModel.popularTVShows()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTvshowsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initViews(binding)

        subscribePopularData(binding)

        return binding.root
    }

    private fun subscribePopularData(binding: FragmentTvshowsBinding) {
        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { resultTVShows ->
                Timber.i("MAS - popular tvshows status: $resultTVShows")

                when (resultTVShows) {
                    is State.Success<PagingData<Screening>> -> {
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

    private fun initViews(binding: FragmentTvshowsBinding) {
        val rvPopularTVShows = binding.rvPopularTVShows

        rvPopularTVShows.apply {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density

            val scaling = resources.getInteger(R.integer.screening_width)
            val columnCount = floor(dpWidth / scaling).toInt()

            layoutManager = GridLayoutManager(context, columnCount)
            adapter = tvShowPageAdapter
        }
    }
}