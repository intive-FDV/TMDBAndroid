package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.databinding.FragmentHomeBinding
import com.intive.tmdbandroid.home.ui.adapters.TVShowPageAdapter
import com.intive.tmdbandroid.home.viewmodel.HomeViewModel
import com.intive.tmdbandroid.home.viewmodel.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private val tvShowPageAdapter = TVShowPageAdapter()

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
            viewModel.uiState.collect { resultTVShows ->
                Log.i("MAS", "popular tvshows status: $resultTVShows")

                when (resultTVShows) {
                    is State.Success -> {
                        binding.tvshowsProgress.visibility = View.GONE
                        tvShowPageAdapter.submitData(resultTVShows.data)
                    }
                    is State.Error -> {
                        binding.tvshowsProgress.visibility = View.GONE
                        Toast.makeText(context, resultTVShows.exception.message, Toast.LENGTH_LONG).show()
                    }
                    is State.Loading -> {
                        binding.tvshowsProgress.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initViews(binding: FragmentHomeBinding) {
        val rvTopTVShows = binding.rvPopularTVShows

        rvTopTVShows.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = tvShowPageAdapter
        }
    }
}
