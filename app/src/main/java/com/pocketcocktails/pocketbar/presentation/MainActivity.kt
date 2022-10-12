package com.pocketcocktails.pocketbar.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.databinding.ActivityMainBinding
import com.pocketcocktails.pocketbar.utils.setVisibility
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private fun isShownNavigation(isShown: Boolean) {
        binding.activityMainBottomNavigationView.setVisibility(isShown)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)
            setupNavigation(this)
        }
    }

    private fun setupNavigation(viewBinding : ActivityMainBinding) {
        val navController = (supportFragmentManager
            .findFragmentById(viewBinding.navHostFragment.id) as NavHostFragment)
            .navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cocktailFragment -> isShownNavigation(false)
                R.id.searchByBaseFragment -> isShownNavigation(false)
                else -> isShownNavigation(true)
            }
        }
        viewBinding.activityMainBottomNavigationView.setupWithNavController(navController)
    }
}