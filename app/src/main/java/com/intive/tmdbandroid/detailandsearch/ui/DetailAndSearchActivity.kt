package com.intive.tmdbandroid.detailandsearch.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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

        val graph = navController
            .navInflater.inflate(R.navigation.nav_graph)

        if(intent.action == Intent.ACTION_VIEW){
            val screeningID = intent.data?.lastPathSegment
            val mediaType = intent.data?.pathSegments?.get(0)
            intent.putExtras(
                bundleOf(
                    "action" to "detail",
                    "screeningID" to screeningID?.toInt(),
                    "isMovieBoolean" to mediaType?.toBoolean()
                )
            )
        }

        val action = intent.extras?.getString("action", "search")
        action.let {
            if (it.equals("search")){
                graph.startDestination = R.id.searchFragmentDest
            } else {
                graph.startDestination = R.id.detailFragmentDest
            }
        }

        navController.setGraph(graph, intent.extras)
    }
}