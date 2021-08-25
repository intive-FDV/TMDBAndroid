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
import androidx.recyclerview.widget.GridLayoutManager
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

    private val tvShowsAdapter = TVShowAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initViews(binding)
        subscribePopularData(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.popularTVShows()
    }

    private fun subscribePopularData(binding: FragmentHomeBinding) {
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
        val rvTopTVShows = binding.rvPopularTVShows

        rvTopTVShows.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = tvShowsAdapter
        }
    }
}
