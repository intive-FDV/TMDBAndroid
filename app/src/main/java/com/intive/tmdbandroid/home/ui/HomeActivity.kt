package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.intive.tmdbandroid.R
import timber.log.Timber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("Lifecycle Activity -> onCreate")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
    }

    override fun onStart() {
        Timber.d("Lifecycle Activity -> onStart")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Timber.d("Lifecycle Activity -> onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        Timber.d("Lifecycle Activity -> onResume")
        super.onResume()
    }

    override fun onPause() {
        Timber.d("Lifecycle Activity -> onPause")
        super.onPause()
    }

    override fun onStop() {
        Timber.d("Lifecycle Activity -> onStop")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.d("Lifecycle Activity -> onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        Timber.d("Lifecycle Activity -> onDestroy")
        super.onDestroy()
    }

}