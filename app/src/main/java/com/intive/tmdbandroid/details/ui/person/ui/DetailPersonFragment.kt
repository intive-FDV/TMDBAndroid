package com.intive.tmdbandroid.details.ui.person.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentDetailPersonBinding
import com.intive.tmdbandroid.details.ui.person.adapter.CombinedCreditsAdapter
import com.intive.tmdbandroid.details.ui.person.viewmodel.DetailPersonViewModel
import com.intive.tmdbandroid.entity.ResultPerson
import com.intive.tmdbandroid.model.Screening
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.floor

@AndroidEntryPoint
class DetailPersonFragment : Fragment() {
    private val viewModel: DetailPersonViewModel by viewModels()

    private var personID: Int? = null

    private lateinit var combinedCreditsAdapter: CombinedCreditsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: DetailPersonFragmentArgs by navArgs()
        personID = args.personID

        val combinedCreditsClickListener = { screening: Screening ->
            val action =
                DetailPersonFragmentDirections.actionDetailPersonFragmentToDetailFragmentDest(
                    screening.id,
                    screening.media_type == "movie"
                )
            val currentDestination = findNavController().currentDestination?.id
            if (currentDestination == R.id.detailPersonFragmentDest) {
                findNavController().navigate(action)
            }
        }
        combinedCreditsAdapter = CombinedCreditsAdapter(combinedCreditsClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailPersonBinding.inflate(inflater, container, false)
        initRecyclerView(binding)
        collectCombineCredits(binding)
        collectDetailPerson(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            personID?.let {
                viewModel.getDetailPerson(it)
                viewModel.getCombinedCredits(it)
            }
        }
    }

    private fun initRecyclerView(binding: FragmentDetailPersonBinding) {
        binding.creditsRecyclerView.apply {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density

            val scaling = resources.getInteger(R.integer.screening_width)
            val columnCount = floor(dpWidth / scaling).toInt()
            minimumHeight = displayMetrics.heightPixels
            layoutManager = GridLayoutManager(context, columnCount)
            adapter = combinedCreditsAdapter
        }
    }

    private fun collectCombineCredits(binding: FragmentDetailPersonBinding) {
        lifecycleScope.launchWhenCreated {
            viewModel.uiStateCombinedCredits.collectLatest { state ->
                when (state) {
                    is State.Waiting -> {
                        binding.coordinatorContainerDetail.isVisible = false
                        binding.layoutLoadingDetail.root.isVisible = true
                    }
                    is State.Loading -> {
                        binding.coordinatorContainerDetail.isVisible = false
                        binding.layoutLoadingDetail.root.isVisible = true
                    }
                    is State.Error -> {
                        binding.coordinatorContainerDetail.isVisible = false
                        binding.layoutLoadingDetail.root.isVisible = false
                        binding.layoutErrorDetail.root.isVisible = true
                    }
                    is State.Success -> {
                        binding.layoutLoadingDetail.root.isVisible = false
                        binding.coordinatorContainerDetail.isVisible = true
                        setupUICombineCredits(binding, state.data)
                    }
                }
            }
        }
    }

    private fun collectDetailPerson(binding: FragmentDetailPersonBinding) {
        lifecycleScope.launchWhenCreated {
            viewModel.uiStateDetailPerson.collectLatest { state ->
                when (state) {
                    is State.Waiting -> {
                        binding.coordinatorContainerDetail.isVisible = false
                        binding.layoutLoadingDetail.root.isVisible = true
                    }
                    is State.Loading -> {
                        binding.coordinatorContainerDetail.isVisible = false
                        binding.layoutLoadingDetail.root.isVisible = true
                    }
                    is State.Error -> {
                        binding.coordinatorContainerDetail.isVisible = false
                        binding.layoutLoadingDetail.root.isVisible = false
                        binding.layoutErrorDetail.root.isVisible = true
                    }
                    is State.Success -> {
                        binding.layoutLoadingDetail.root.isVisible = false
                        binding.coordinatorContainerDetail.isVisible = true
                        setupUIDetailPerson(binding, state.data)
                    }
                }
            }
        }
    }

    private fun setupUICombineCredits(
        binding: FragmentDetailPersonBinding,
        screenings: List<Screening>
    ) {
        if (screenings.isNotEmpty())
            combinedCreditsAdapter.submitList(screenings)
        else {
            binding.layoutErrorDetail.root.isVisible = true
        }
    }

    private fun setupUIDetailPerson(
        binding: FragmentDetailPersonBinding,
        resultPerson: ResultPerson
    ) {
        setImage(resultPerson.profile_path, binding)
        setPopularity(resultPerson.popularity, binding)
        setToolbar(resultPerson.name, binding)
        setBiography(resultPerson.biography, binding)
    }

    private fun setImage(
        profilePath: String?,
        binding: FragmentDetailPersonBinding
    ) {
        val options = RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)

        val posterURL = resources.getString(R.string.base_imageURL) + profilePath

        Glide.with(this)
            .load(posterURL)
            .apply(options)
            .into(binding.imageDetailImageView)
    }

    private fun setPopularity(
        popularity: Double,
        binding: FragmentDetailPersonBinding
    ) {
        val percentage = (popularity * 10).toInt()

        binding.popularityRatingNumber.text =
            requireContext().resources.getString(R.string.popularity, percentage)

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

    private fun setToolbar(name: String, binding: FragmentDetailPersonBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        val toolbar = binding.toolbar

        toolbar.title = name

        binding.collapsingToolbarLayout.setupWithNavController(
            toolbar,
            navController,
            appBarConfiguration
        )

        toolbar.navigationIcon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            if (!navController.navigateUp()) {
                activity?.finish()
            }
        }
    }

    private fun setBiography(biography: String, binding: FragmentDetailPersonBinding) {
        if (biography.isBlank()) {
            binding.biographyTextView.isVisible = false
            binding.biographyDetailTextView.isVisible = false
        } else {
            binding.biographyDetailTextView.text = biography
        }
    }

}