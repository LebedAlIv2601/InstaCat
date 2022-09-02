package com.lebedaliv2601.instacat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lebedaliv2601.instacat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fcvContainer
        ) as NavHostFragment
        val navController = navHostFragment.navController

        binding?.bnvAppBottomNavigation?.setupWithNavController(navController)

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}