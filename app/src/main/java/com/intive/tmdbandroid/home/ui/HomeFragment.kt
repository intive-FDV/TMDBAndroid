package com.intive.tmdbandroid.home.ui

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.state.Resource
import com.intive.tmdbandroid.databinding.FragmentHomeBinding
import com.intive.tmdbandroid.home.ui.adapters.MovieAdapter
import com.intive.tmdbandroid.home.ui.adapters.TVShowAdapter
import com.intive.tmdbandroid.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private val movieAdapter = MovieAdapter(arrayListOf())
    private val tvShowsAdapter = TVShowAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initViews(binding)
//        subscribeUi(binding)
        subscribePopularData(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sample()
        viewModel.popularMovies()
        viewModel.popularTVShows()
    }

    /*private fun subscribeUi(binding: FragmentHomeBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.sampleFlow.collectLatest { sampleResource ->
                when (sampleResource.status) {
                    Resource.Status.SUCCESS -> {
                        binding.dummy.text = sampleResource.data?.dummy
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(context, sampleResource.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                    }
                    Resource.Status.FAILURE -> {
                        Toast.makeText(context, sampleResource.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }*/

    private fun subscribePopularData(binding: FragmentHomeBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.popularMoviesFlow.collectLatest { resultMovies ->
                Log.i("MAS", "popular movies status: ${resultMovies.status}")

                when (resultMovies.status) {
                    Resource.Status.SUCCESS -> {
                        binding.moviesProgress.visibility = View.GONE
                        resultMovies.data?.movies?.let { movieAdapter.refresh(it) }
                    }
                    Resource.Status.ERROR -> {
                        binding.moviesProgress.visibility = View.GONE
                        Toast.makeText(context, resultMovies.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                        binding.moviesProgress.visibility = View.VISIBLE
                    }
                    Resource.Status.FAILURE -> {
                        binding.moviesProgress.visibility = View.GONE
                        Toast.makeText(context, resultMovies.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.popularTVShowsFlow.collectLatest { resultTVShows ->
                Log.i("MAS", "popular tvshows status: ${resultTVShows.status}")

                when (resultTVShows.status) {
                    Resource.Status.SUCCESS -> {
                        binding.tvshowsProgress.visibility = View.GONE
                        resultTVShows.data?.TVShows?.let { tvShowsAdapter.refresh(it) }
                    }
                    Resource.Status.ERROR -> {
                        binding.tvshowsProgress.visibility = View.GONE
                        Toast.makeText(context, resultTVShows.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                        binding.tvshowsProgress.visibility = View.VISIBLE
                    }
                    Resource.Status.FAILURE -> {
                        binding.tvshowsProgress.visibility = View.GONE
                        Toast.makeText(context, resultTVShows.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun initViews(binding: FragmentHomeBinding) {
        val rvTopMovies = binding.rvPopularMovies
        val rvTopTVShows = binding.rvPopularTVShows

        rvTopMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieAdapter
        }
        rvTopTVShows.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tvShowsAdapter
        }

        val topMoviesText = binding.popularMoviesText
        val topTVShowsText = binding.popularTvshowsText

        val moviesTextwidth = topMoviesText.paint.measureText(topMoviesText.text.toString())
        val tvShowsTextwidth = topTVShowsText.paint.measureText(topMoviesText.text.toString())

        val darkseaGreen = ContextCompat.getColor(binding.root.context, R.color.darksea_green)
        val lightseaGreen = ContextCompat.getColor(binding.root.context, R.color.lightsea_green)

        val moviesTextShader = LinearGradient(0f, 0f, moviesTextwidth, 0f,
            darkseaGreen,
            lightseaGreen,
            Shader.TileMode.CLAMP)
        val tvShowsTextShader = LinearGradient(0f, 0f, tvShowsTextwidth, 0f,
            darkseaGreen,
            lightseaGreen,
            Shader.TileMode.CLAMP)

        topMoviesText.setTextColor(darkseaGreen)
        topMoviesText.paint.shader = moviesTextShader
        topTVShowsText.setTextColor(darkseaGreen)
        topTVShowsText.paint.shader = tvShowsTextShader
    }

}
