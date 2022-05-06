package com.pocketcocktails.pocketbar.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.databinding.FragmentHomeBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() = with(receiver = binding) {
        binding.rumImg.setOnClickListener {
            val fragment = SearchByBaseFragment.newInstance(getString(R.string.rum_text))
            showCocktail(fragment)
        }
        binding.ginImg.setOnClickListener {
            val fragment = SearchByBaseFragment.newInstance(getString(R.string.gin_text))
            showCocktail(fragment)
        }
        binding.tequilaImg.setOnClickListener {
            val fragment = SearchByBaseFragment.newInstance(getString(R.string.tequila_text))
            showCocktail(fragment)
        }
        binding.whiskeyImg.setOnClickListener {
            val fragment = SearchByBaseFragment.newInstance(getString(R.string.whiskey_text))
            showCocktail(fragment)
        }
        binding.vodkaImg.setOnClickListener {
            val fragment = SearchByBaseFragment.newInstance(getString(R.string.vodka_text))
            showCocktail(fragment)
        }
        binding.brandyImg.setOnClickListener {
            val fragment = SearchByBaseFragment.newInstance(getString(R.string.brandy_text))
            showCocktail(fragment)
        }
    }

    private fun showCocktail(fragmentSearch: SearchByBaseFragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragmentSearch)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}