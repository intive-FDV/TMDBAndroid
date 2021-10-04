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
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentMoviesBinding
import com.intive.tmdbandroid.detailandsearch.ui.DetailAndSearchActivity
import com.intive.tmdbandroid.home.ui.adapters.ScreeningPageAdapter
import com.intive.tmdbandroid.home.viewmodel.MoviesViewModel
import com.intive.tmdbandroid.model.Screening
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlin.math.floor

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private val viewModel: MoviesViewModel by navGraphViewModels(R.id.bottom_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var moviePageAdapter: ScreeningPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clickListener = { screening: Screening ->
            val intent = Intent(requireActivity(), DetailAndSearchActivity::class.java)
            intent.putExtras(
                bundleOf(
                    (context?.getString(R.string.intent_extra_key_action) ?: "") to (context?.getString(R.string.intent_extra_key_action_detail) ?: ""),
                    (context?.getString(R.string.intent_extra_key_screening_id) ?: "") to screening.id,
                    (context?.getString(R.string.intent_extra_key_is_movie) ?: "") to true
                )
            )
            requireActivity().startActivity(intent)
        }
        moviePageAdapter = ScreeningPageAdapter(clickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMoviesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initViews(binding)
        subscribePopularData(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null)
            viewModel.popularMovies()
    }

    private fun subscribePopularData(binding: FragmentMoviesBinding) {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { resultMovies ->

                when (resultMovies) {
                    is State.Success<PagingData<Screening>> -> {
                        binding.layoutError.errorContainer.visibility = View.GONE
                        binding.layoutProgressbar.root.visibility = View.GONE

                        moviePageAdapter.submitData(resultMovies.data)
                    }
                    is State.Error -> {
                        binding.layoutProgressbar.root.visibility = View.GONE
                        binding.layoutError.errorContainer.visibility = View.VISIBLE
                    }
                    is State.Loading -> {
                        binding.layoutProgressbar.root.visibility = View.VISIBLE
                        binding.layoutError.errorContainer.visibility = View.GONE
                    }
                    else -> throw RuntimeException(context?.getString(R.string.state_error))
                }
            }
        }
    }

    private fun initViews(binding: FragmentMoviesBinding) {
        val rvPopularMovies = binding.rvPopularMovies

        rvPopularMovies.apply {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density

            val scaling = resources.getInteger(R.integer.screening_width)
            val columnCount = floor(dpWidth / scaling).toInt()

            layoutManager = GridLayoutManager(context, columnCount)
            adapter = moviePageAdapter
        }
    }
}