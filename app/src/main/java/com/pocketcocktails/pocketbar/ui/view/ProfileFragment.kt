package com.pocketcocktails.pocketbar.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pocketcocktails.pocketbar.databinding.FragmentProfieBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfieBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfieBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}
