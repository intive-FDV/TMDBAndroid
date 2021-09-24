package com.intive.tmdbandroid.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.intive.tmdbandroid.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAndSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_and_search)
        val args = intent.extras
        args?.let {
            val screeningId = it.getInt("screeningID")

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            if (screeningId != 0){
                navController.navigate(R.id.action_emptyFragment_to_detailFragment,
                    bundleOf("screeningID" to screeningId))
            }else {
                navController.navigate(R.id.action_emptyFragment_to_searchFragment)
            }
        }
    }
}