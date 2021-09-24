package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.FragmentMoviesBinding

class MoviesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMoviesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.title = getString(R.string.popular_movies)
        //setupToolbar(binding)

        return binding.root
    }

//    private fun setupToolbar(binding: FragmentMoviesBinding) {
//        val navController = findNavController()
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.fragmentMoviesToolbar.setupWithNavController(navController, appBarConfiguration)
//        binding.fragmentMoviesToolbar.inflateMenu(R.menu.options_menu)
//        binding.fragmentMoviesToolbar.setOnMenuItemClickListener{
//            binding.fragmentMoviesToolbar.findNavController().navigate(R.id.action_homeFragmentDest_to_searchFragment)
//            true
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}