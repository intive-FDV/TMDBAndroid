package com.intive.tmdbandroid.home.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ActivityHomeBinding
import com.intive.tmdbandroid.detailandsearch.ui.DetailAndSearchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeToolbar)

        val navView: BottomNavigationView = binding.bottomNav

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHost.navController

        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, DetailAndSearchActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        "action" to "search"
                    )
                )
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
