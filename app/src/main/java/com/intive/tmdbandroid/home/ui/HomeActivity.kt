package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.intive.tmdbandroid.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

}