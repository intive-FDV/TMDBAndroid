package com.intive.tmdbandroid.home.ui

import android.content.Context
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

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private val tvShowPageAdapter = TVShowPageAdapter()

    private lateinit var binding: FragmentHomeBinding

    override fun onAttach(context: Context) {
        Timber.d("Lifecycle Fragment -> onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("Lifecycle Fragment -> onCreate")
        super.onCreate(savedInstanceState)

        viewModel.popularTVShows()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Timber.d("Lifecycle Fragment -> onCreateView")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initViews()
        subscribePopularData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("Lifecycle Fragment -> onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onStart() {
        Timber.d("Lifecycle Fragment -> onStart")
        super.onStart()
    }

    override fun onResume() {
        Timber.d("Lifecycle Fragment -> onResume")
        super.onResume()
    }

    override fun onPause() {
        Timber.d("Lifecycle Fragment -> onPause")
        super.onPause()
    }

    override fun onStop() {
        Timber.d("Lifecycle Fragment -> onStop")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.d("Lifecycle Fragment -> onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Timber.d("Lifecycle Fragment -> onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.d("Lifecycle Fragment -> onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Timber.d("Lifecycle Fragment -> onDetach")
        super.onDetach()
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
                when (resultTVShows) {
                    is State.Success<PagingData<TVShow>> -> {
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

    private fun initViews() {
        val rvTopTVShows = binding.rvPopularTVShows

        rvTopTVShows.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = tvShowPageAdapter
        }
    }
}
