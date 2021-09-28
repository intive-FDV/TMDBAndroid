package com.intive.tmdbandroid.detailandsearch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ActivityDetailAndSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAndSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailAndSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_detail_and_search) as NavHostFragment
        val navController = navHost.navController

        val graph = navHost.navController
            .navInflater.inflate(R.navigation.nav_graph)
        graph.startDestination = R.id.searchFragmentDest

        navHost.navController.graph = graph
    }
}