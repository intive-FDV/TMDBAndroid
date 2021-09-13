package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
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
    }

    private fun subscribePopularData() {
        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { resultTVShows ->
                Timber.i("MAS - popular tvshows status: $resultTVShows")

                when (resultTVShows) {
                    is State.Success<PagingData<TVShow>> -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE

                        updateMockWatchlist()

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

    private fun updateMockWatchlist() {
        val list = listOf(
            TVShow("", emptyList(),"2021-09-13", emptyList(),0,"","Some TV Show 0", 1, 1, "","","","",2.0,10),
            TVShow("", emptyList(),"2021-09-13", emptyList(),1,"","Some TV Show 1", 2, 2, "","","","",4.0,10),
            TVShow("", emptyList(),"2021-09-13", emptyList(),2,"","Some TV Show 2", 3, 3, "","","","",6.0,10),
            TVShow("", emptyList(),"2021-09-13", emptyList(),3,"","Some TV Show 3", 4, 4, "","","","",8.0,10)
        )

        tvShowPageAdapter.refreshWatchlistAdapter(list)
    }

    private fun initViews() {
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
