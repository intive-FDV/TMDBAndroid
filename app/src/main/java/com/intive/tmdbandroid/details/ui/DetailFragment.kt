package com.intive.tmdbandroid.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.intive.tmdbandroid.model.Screening
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import android.util.TypedValue

import android.widget.LinearLayout
import android.widget.TextView


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var isSaveOnWatchlist: Boolean = false
    private var screeningItemId: Int? = null

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: DetailFragmentArgs by navArgs()
        screeningItemId = args.screeningID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        collectScreeningDetailFromViewModel(binding)
        collectWatchlistDataFromViewModel(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailFragmentArgs by navArgs()
        screeningItemId?.let {
            Timber.i("MAS - screeningID: $it")
            if(savedInstanceState==null){
                if (args.isMovieBoolean) viewModel.movie(it)
                else viewModel.tVShows(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Glide.get(requireContext()).clearMemory()
    }

    private fun collectScreeningDetailFromViewModel(binding: FragmentDetailBinding) {
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

    private fun collectWatchlistDataFromViewModel(binding: FragmentDetailBinding) {
        lifecycleScope.launch {
            viewModel.watchlistUIState.collectLatest {
                when (it) {
                    is State.Success -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        selectOrUnselectWatchlistFav(binding, it.data)
                        isSaveOnWatchlist = it.data
                    }
                    State.Error -> {
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        binding.layoutErrorDetail.errorContainer.visibility = View.VISIBLE
                        binding.coordinatorContainerDetail.visibility = View.VISIBLE
                    }
                    State.Loading -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupUI(binding: FragmentDetailBinding, screening: Screening) {

        setImages(binding, screening)

        screening.release_date?.let { setDate(binding, it) }

        setPercentageToCircularPercentage(binding, screening.vote_average)

        setupToolbar(binding, screening)

        binding.toolbar.title = screening.name

        binding.statusDetailTextView.text = screening.status

        val genresListText = screening.genres?.map {
            it.name
        }.toString()
        val genresTextWithoutCharacters = genresListText.subSequence(1, genresListText.lastIndex)
        binding.genresDetailTextView.text = genresTextWithoutCharacters

        binding.numberOfEpisodesDetailTextView.visibility = View.GONE
        binding.numberOfSeasonsDetailTextView.visibility = View.GONE

        binding.numberOfSeasonsDetailTextView.text =
            screening.number_of_seasons?.let {
                binding.numberOfSeasonsDetailTextView.visibility = View.VISIBLE
                resources.getQuantityString(
                    R.plurals.numberOfSeasons,
                    it,
                    it
                )
            }
        binding.numberOfEpisodesDetailTextView.text =
            screening.number_of_episodes?.let {
                binding.numberOfEpisodesDetailTextView.visibility = View.VISIBLE
                resources.getQuantityString(
                    R.plurals.numberOfEpisodes,
                    it,
                    it
                )
            }

        if(screening.overview.isEmpty()) binding.overviewDetailTextView.text = resources.getString(R.string.no_overview)
        else binding.overviewDetailTextView.text = screening.overview
        binding.coordinatorContainerDetail.visibility = View.VISIBLE

        screeningItemId?.let { viewModel.existAsFavorite(it) }
    }

    private fun setPercentageToCircularPercentage(
        binding: FragmentDetailBinding,
        voteAverage: Double
    ) {
        val percentage = (voteAverage * 10).toInt()

        binding.popularityRatingNumber.text = "$percentage%"

        val context = binding.root.context

        when {
            percentage < 25 -> binding.popularityThumbIcon.imageTintList =
                ContextCompat.getColorStateList(context, R.color.red)
            percentage < 45 -> binding.popularityThumbIcon.imageTintList =
                ContextCompat.getColorStateList(context, R.color.orange)
            percentage < 75 -> binding.popularityThumbIcon.imageTintList =
                ContextCompat.getColorStateList(context, R.color.yellow)
            else -> binding.popularityThumbIcon.imageTintList =
                ContextCompat.getColorStateList(context, R.color.green)
        }
    }

    private fun setDate(binding: FragmentDetailBinding, firstAirDate: String) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAirDate)
            val stringDate =
                date?.let { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it) }
            binding.firstAirDateDetailTextView.text = stringDate
        } catch (e: Exception) {
            binding.firstAirDateDetailTextView.text = ""
        }
    }

    private fun setImages(binding: FragmentDetailBinding, screening: Screening) {
        val options = RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)

        val posterURL = resources.getString(R.string.base_imageURL) + screening.poster_path
        val backdropPosterURL = resources.getString(R.string.base_imageURL) + screening.backdrop_path

        val glide = Glide.with(this)

        glide.load(posterURL)
            .apply(options)
            .into(binding.imageDetailImageView)

        glide.load(backdropPosterURL)
            .apply(options)
            .into(binding.backgroundImageToolbarLayout)

        if (screening.networks.isNotEmpty()){
            screening.networks.forEach {

                val networkImg = ImageView(binding.networkContainer.context)
                glide.load(resources.getString(R.string.base_imageURL) + it.logo_path)
                    .apply(options)
                    .into(networkImg)

                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                lp.setMargins(0, 0, calculateDP(40f), 0)

                networkImg.layoutParams = lp
                networkImg.layoutParams.width = calculateDP(100f)

                binding.networkContainer.addView(networkImg)
            }
        } else {
            binding.networksHeader.visibility = View.GONE
        }
    }

    private fun setupToolbar(binding: FragmentDetailBinding, screening: Screening) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.watchlist_favorite_detail_fragment)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.ic_heart_watchlist -> {
                    if (!isSaveOnWatchlist) {
                        viewModel.addToWatchlist(screening)
                    } else {
                        viewModel.deleteFromWatchlist(screening)
                    }
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

        toolbar.navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            if (navController.navigateUp()) {
                navController.popBackStack()
            }else {
                activity?.finish()
            }
        }
    }

    private fun selectOrUnselectWatchlistFav(binding: FragmentDetailBinding, isFav: Boolean) {
        val watchlistItem = binding.toolbar.menu.findItem(R.id.ic_heart_watchlist)
        if (isFav) {
            watchlistItem.icon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_heart_selected)
        } else watchlistItem.icon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_heart_unselected)

    }

    private fun calculateDP(value: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            resources.displayMetrics
        ).toInt()
    }

}