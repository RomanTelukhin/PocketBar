package com.pocketcocktails.pocketbar.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.databinding.FragmentHomeBinding
import com.pocketcocktails.pocketbar.presentation.MainActivity
import com.pocketcocktails.pocketbar.presentation.search.SearchByBaseFragment
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.rumImg.setOnClickListener(this)
        binding.ginImg.setOnClickListener(this)
        binding.tequilaImg.setOnClickListener(this)
        binding.whiskeyImg.setOnClickListener(this)
        binding.vodkaImg.setOnClickListener(this)
        binding.brandyImg.setOnClickListener(this)
        return binding.root
    }

    private fun showCocktail(fragmentSearch: SearchByBaseFragment) {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.container, fragmentSearch)
//        transaction.addToBackStack(null)
//        transaction.commit()
//        activity?.findNavController(R.id.activity_main_nav_host_fragment).navigate()
    }

    override fun onClick(v: View) {
        val baseText = when (v) {
            binding.rumImg -> getString(R.string.rum_text)
            binding.ginImg -> getString(R.string.gin_text)
            binding.tequilaImg -> getString(R.string.tequila_text)
            binding.whiskeyImg -> getString(R.string.whiskey_text)
            binding.vodkaImg -> getString(R.string.vodka_text)
            binding.brandyImg -> getString(R.string.brandy_text)
            else -> EMPTY_STRING
        }
        val action = HomeFragmentDirections.actionHomeFragmentToSearchByBaseFragment(baseText)
        v.findNavController().navigate(action)
//        val fragment = SearchByBaseFragment.newInstance(baseText)
//        showCocktail(fragment)
    }

}