package com.pocketcocktails.pocketbar.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomMenu.setOnItemSelectedListener(mOnNavigationItemSelectedListener)
        binding.bottomMenu.selectedItemId = R.id.home
        setContentView(binding.root)
    }

    private val mOnNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openFragment(HomeFragment.newInstance())
                    return@OnItemSelectedListener true
                }
                R.id.favorites -> {
                    openFragment(FavoritesFragment.newInstance())
                    return@OnItemSelectedListener true
                }
                R.id.search -> {
                    openFragment(SearchByQueryFragment.newInstance())
                    return@OnItemSelectedListener true
                }
                R.id.profile -> {
                    openFragment(ProfileFragment.newInstance())
                    return@OnItemSelectedListener true
                }
            }
            false
        }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}