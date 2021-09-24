package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentMoviesBinding
import com.intive.tmdbandroid.home.ui.adapters.ScreeningPageAdapter
import com.intive.tmdbandroid.home.viewmodel.MoviesViewModel
import com.intive.tmdbandroid.model.Screening
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import kotlin.math.floor
import android.os.Parcelable
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private val viewModel: MoviesViewModel by viewModels()

    private val clickListener = { screening: Screening ->
//        val action = WatchlistFragmentDirections.actionHomeFragmentDestToTVShowDetail(tvShow.id)
//        findNavController().navigate(action)
        Toast.makeText(context, screening.name, Toast.LENGTH_SHORT).show()
    }
    private val moviePageAdapter = ScreeningPageAdapter(clickListener)
    private lateinit var rvPopularMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            Timber.i("MAS - instance = null")
            viewModel.popularMovies()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMoviesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.title = getString(R.string.popular_movies)

        initViews(binding)

        subscribePopularData(binding)

        return binding.root
    }

    private fun subscribePopularData(binding: FragmentMoviesBinding) {
        binding.layoutProgressbar.progressBar.visibility = View.VISIBLE
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { resultTVShows ->
                Timber.i("MAS - popular tvshows status: $resultTVShows")

                when (resultTVShows) {
                    is State.Success<PagingData<Screening>> -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutProgressbar.progressBar.visibility = View.GONE

                        moviePageAdapter.submitData(resultTVShows.data)

                        if (moviePageAdapter.itemCount == 0) {
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

    private fun initViews(binding: FragmentMoviesBinding) {
        rvPopularMovies = binding.rvPopularMovies

        rvPopularMovies.apply {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            Timber.i("MAS - dpWidth: $dpWidth")

            val scaling = resources.getInteger(R.integer.screening_width)
            val columnCount = floor(dpWidth / scaling).toInt()
            Timber.i("MAS - columnCount: $columnCount")

            layoutManager = GridLayoutManager(context, columnCount)
            adapter = moviePageAdapter
        }
    }
}