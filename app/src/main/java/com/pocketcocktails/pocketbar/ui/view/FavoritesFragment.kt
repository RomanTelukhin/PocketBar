package com.pocketcocktails.pocketbar.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.databinding.FragmentFavoritesBinding
import com.pocketcocktails.pocketbar.ui.actions.UserActionShowFavorites
import com.pocketcocktails.pocketbar.ui.adapter.FavoritesAdapter
import com.pocketcocktails.pocketbar.ui.viewmodel.FavoritesViewModel
import com.pocketcocktails.pocketbar.ui.viewstate.FavoritesViewState
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.appComponent
import com.pocketcocktails.pocketbar.utils.setVisibility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: FavoritesAdapter

    @Inject
    lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        requireContext().appComponent.inject(this)
        setupView()
        renderView()
        return binding.root
    }

    private fun setupView() =
        with(receiver = binding) {
            favoritesAdapter = FavoritesAdapter(
                onItemClick = { cocktailListItem -> onItemClick(cocktailListItem) },
                onFavoriteClick = { cocktailListItem -> onFavoriteClick(cocktailListItem) }
            )
            favoritesAdapter.setHasStableIds(true)
            favoritesRecycler.layoutManager = LinearLayoutManager(requireContext())
            favoritesRecycler.adapter = favoritesAdapter
            favoritesViewModel.userActionChannel.tryEmit(UserActionShowFavorites.ShowFavorites)
        }

    private fun renderView() {
        lifecycleScope
            .launchWhenStarted {
                favoritesViewModel
                    .favoriteCocktailsViewState
                    .collect {
                        Timber.d("$TEST_LOG_TAG renderView FavoritesViewState: $it")
                        when (val state = it.items) {
                            is FavoritesViewState.Items.Loading -> showLoading()
                            is FavoritesViewState.Items.Drinks -> showDrinks(state)
                            is FavoritesViewState.Items.Error -> showError(state)
                            is FavoritesViewState.Items.Idle -> {}
                        }
                    }
            }
    }

    private fun showLoading() {
        binding.progressBar.setVisibility(true)
        binding.infoTextView.setVisibility(false)
        binding.favoritesRecycler.setVisibility(false)
    }

    private fun showDrinks(result: FavoritesViewState.Items.Drinks) {
        binding.progressBar.setVisibility(false)
        binding.infoTextView.setVisibility(false)
        binding.favoritesRecycler.setVisibility(true)
        favoritesAdapter.listCocktails = result.drinksList
    }

    private fun showError(result: FavoritesViewState.Items.Error) {
        binding.progressBar.setVisibility(false)
        binding.favoritesRecycler.setVisibility(false)
        binding.infoTextView.setVisibility(true)
        binding.infoTextView.text = result.error
    }

    private fun onItemClick(item: CocktailListItem) {
        val fragment = CocktailFragment.newInstance(item.idDrink)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun onFavoriteClick(item: CocktailListItem) {
        Timber.d("favorite screen onFavoriteClick item: ${item.strDrink} is ${item.isFavorite}")
        favoritesViewModel.userActionChannel.tryEmit(UserActionShowFavorites.OnFavoritesChanged(item))
    }

    companion object {
        fun newInstance(): FavoritesFragment = FavoritesFragment()
    }
}