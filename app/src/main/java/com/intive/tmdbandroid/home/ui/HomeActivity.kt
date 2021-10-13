package com.intive.tmdbandroid.home.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ActivityHomeBinding
import com.intive.tmdbandroid.detailandsearch.ui.DetailAndSearchActivity
import dagger.hilt.android.AndroidEntryPoint

const val THEME_DEFAULT = 0

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        val context = binding.root.context

        setContentView(binding.root)
        setSupportActionBar(binding.homeToolbar)

        val navView: BottomNavigationView = binding.bottomNav

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHost.navController

        navView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.tvShowsDest, R.id.watchlistDest,  R.id.moviesDest)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        val themePreference = getPreferences(MODE_PRIVATE).getInt(baseContext.getString(R.string.theme_shared_pref), THEME_DEFAULT)
        if(themePreference != THEME_DEFAULT){
            AppCompatDelegate.setDefaultNightMode(themePreference)
        }

        if(intent.action == Intent.ACTION_VIEW){
            val shareIntent = Intent(this, DetailAndSearchActivity::class.java)
            val screeningID = intent.data?.lastPathSegment?.toInt()
            val mediaType = intent.data?.pathSegments?.get(0) == context.getString(R.string.screening_movie_type)
            shareIntent.putExtras(
                bundleOf(
                    context.getString(R.string.intent_extra_key_action) to context.getString(R.string.intent_extra_key_action_detail),
                    context.getString(R.string.intent_extra_key_screening_id) to screeningID,
                    context.getString(R.string.intent_extra_key_is_movie) to mediaType
                )
            )
            startActivity(shareIntent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        if(getPreferences(MODE_PRIVATE).getInt(baseContext.getString(R.string.theme_shared_pref), THEME_DEFAULT)
            == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.findItem(R.id.switch_theme).icon = AppCompatResources.getDrawable(baseContext, R.drawable.ic_bedtime)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        return when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, DetailAndSearchActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        baseContext.getString(R.string.intent_extra_key_action) to baseContext.getString(R.string.intent_extra_key_action_default)
                    )
                )
                startActivity(intent)
                true
            }
            R.id.switch_theme  -> {
                val themePreference = getPreferences(MODE_PRIVATE)
                println("shared preference: ${themePreference.getInt(baseContext.getString(R.string.theme_shared_pref), THEME_DEFAULT)}")
                when (baseContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        with(themePreference.edit()){
                            putInt(baseContext.getString(R.string.theme_shared_pref), AppCompatDelegate.MODE_NIGHT_YES)
                            apply()
                        }
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        with(themePreference.edit()){
                            putInt(baseContext.getString(R.string.theme_shared_pref), AppCompatDelegate.MODE_NIGHT_NO)
                            apply()
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
