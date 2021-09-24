package com.intive.tmdbandroid.home.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.FragmentTvshowsBinding

class TVShowsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTvshowsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.title = getString(R.string.popular_tvShows)
        //setupToolbar(binding)

        return binding.root
    }

    private fun setupToolbar(binding: FragmentTvshowsBinding) {
//        binding.fragmentTvshowsToolbar.setTitle(R.string.popular_tvShows)
//        binding.fragmentTvshowsToolbar.setTitleTextColor(ContextCompat.getColor(requireContext(),R.color.secondaryColor))

//        val navController = findNavController()
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.fragmentTvshowsToolbar.setupWithNavController(navController, appBarConfiguration)
//        binding.fragmentTvshowsToolbar.inflateMenu(R.menu.options_menu)
//        binding.fragmentTvshowsToolbar.setOnMenuItemClickListener{
//            binding.fragmentTvshowsToolbar.findNavController().navigate(R.id.action_homeFragmentDest_to_searchFragment)
//            true
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}