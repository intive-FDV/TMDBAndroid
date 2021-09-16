package com.intive.tmdbandroid.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentDetailBinding
import com.intive.tmdbandroid.details.viewmodel.DetailsViewModel
import com.intive.tmdbandroid.model.TVShow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var isSaveOnWatchlist: Boolean = false
    private var tvShowId: Int? = null

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: DetailFragmentArgs by navArgs()
        tvShowId = args.screeningID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        collectDataFromViewModel(binding)
        setupToolbar(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvShowId?.let {
            viewModel.tVShows(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Glide.get(requireContext()).clearMemory()
    }

    private fun collectDataFromViewModel(binding: FragmentDetailBinding) {
        binding.coordinatorContainerDetail.visibility = View.INVISIBLE
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                when (state) {
                    is State.Success -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        setupUI(binding, state.data)
                    }
                    is State.Error -> {
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        binding.layoutErrorDetail.errorContainer.visibility = View.VISIBLE
                        binding.coordinatorContainerDetail.visibility = View.VISIBLE
                    }
                    is State.Loading -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupUI(binding: FragmentDetailBinding, tvShow: TVShow) {

        setImages(binding, tvShow)

        tvShow.first_air_date?.let { setDate(binding, it) }

        setPercentageToCircularPercentage(binding, tvShow.vote_average)

        binding.toolbar.title = tvShow.name

        binding.statusDetailTextView.text = tvShow.status

        val genresListText = tvShow.genres.map {
            it.name
        }.toString()
        val genresTextWithoutCharacters = genresListText.subSequence(1, genresListText.lastIndex)
        binding.genresDetailTextView.text = genresTextWithoutCharacters

        binding.numberOfSeasonsDetailTextView.text =
            tvShow.number_of_seasons?.let {
                resources.getQuantityString(
                    R.plurals.numberOfSeasons,
                    it,
                    it
                )
            }
        binding.numberOfEpisodesDetailTextView.text =
            tvShow.number_of_episodes?.let {
                resources.getQuantityString(
                    R.plurals.numberOfEpisodes,
                    it,
                    it
                )
            }

        binding.overviewDetailTextView.text = tvShow.overview
        binding.coordinatorContainerDetail.visibility = View.VISIBLE
    }

    private fun setPercentageToCircularPercentage(
        binding: FragmentDetailBinding,
        voteAverage: Double
    ) {
        val percentage = (voteAverage * 10).toInt()

        binding.circularPercentage.progress = percentage

        val context = binding.root.context

        when {
            percentage < 25 -> binding.circularPercentage.progressTintList = ContextCompat.getColorStateList(context, R.color.red)
            percentage < 45 -> binding.circularPercentage.progressTintList = ContextCompat.getColorStateList(context, R.color.orange)
            percentage < 75 -> binding.circularPercentage.progressTintList = ContextCompat.getColorStateList(context, R.color.yellow)
            else -> binding.circularPercentage.progressTintList = ContextCompat.getColorStateList(context, R.color.green)
        }
        binding.screeningPopularity.text = resources.getString(R.string.popularity, percentage)
    }

    private fun setDate(binding: FragmentDetailBinding, firstAirDate: String) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAirDate)
            val stringDate = date?.let { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it) }
            binding.firstAirDateDetailTextView.text = stringDate
        } catch (e: Exception) {
            binding.firstAirDateDetailTextView.text = ""
        }
    }

    private fun setImages(binding: FragmentDetailBinding, tvShow: TVShow) {
        val options = RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)

        val posterURL = resources.getString(R.string.base_imageURL) + tvShow.poster_path
        val backdropPosterURL = resources.getString(R.string.base_imageURL) + tvShow.backdrop_path

        val glide = Glide.with(this)

        glide.load(posterURL)
            .apply(options)
            .into(binding.imageDetailImageView)

        glide.load(backdropPosterURL)
            .apply(options)
            .into(binding.backgroundImageToolbarLayout)
    }

    private fun setupToolbar(binding: FragmentDetailBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.watchlist_favorite_detail_fragment)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.ic_heart_watchlist -> {
                    selectOrUnselectWatchlistFav(binding)
                    true
                }
                else -> false
            }
        }
        binding.collapsingToolbarLayout.setupWithNavController(
            toolbar,
            navController,
            appBarConfiguration
        )
        binding.appBarLayoutDetail.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset < -500) {
                binding.popularityCard.visibility = View.INVISIBLE
            } else binding.popularityCard.visibility = View.VISIBLE
        })
    }

    private fun selectOrUnselectWatchlistFav(binding: FragmentDetailBinding) {
        isSaveOnWatchlist = !isSaveOnWatchlist
        val watchlistItem = binding.toolbar.menu.findItem(R.id.ic_heart_watchlist)
        if (isSaveOnWatchlist){
            watchlistItem.icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_heart_selected)
        }else {
            watchlistItem.icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_heart_unselected)
        }
    }
}