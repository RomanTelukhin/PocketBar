package com.pocketcocktails.pocketbar.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.CocktailsApp
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.databinding.FragmentSearchBinding
import com.pocketcocktails.pocketbar.ui.actions.UserActionSearchByQuery
import com.pocketcocktails.pocketbar.ui.adapter.DrinksAdapter
import com.pocketcocktails.pocketbar.ui.view.iview.SearchView
import com.pocketcocktails.pocketbar.ui.viewmodel.SearchByQueryViewModel
import com.pocketcocktails.pocketbar.ui.viewstate.SearchViewState
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.appComponent
import com.pocketcocktails.pocketbar.utils.setVisibility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByQueryFragment : Fragment(), SearchView {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var drinksAdapter: DrinksAdapter

    @Inject
    lateinit var searchByQueryViewModel: SearchByQueryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        requireContext().appComponent.inject(this)
        setupView()
        renderView()
        return binding.root
    }

    override fun setupView() =
        with(receiver = binding) {
            drinksAdapter = DrinksAdapter(
                onItemClick = { cocktailListItem -> onItemClick(cocktailListItem) },
                onFavoriteClick = { cocktailListItem -> onFavoriteClick(cocktailListItem) }
            )
            drinksAdapter.setHasStableIds(true)
            cocktailsRecycler.layoutManager = LinearLayoutManager(requireContext())
            cocktailsRecycler.adapter = drinksAdapter
            searchView.isIconified = false
            searchView.setOnQueryTextListener(
                object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean = false
                    override fun onQueryTextChange(newText: String): Boolean {
                        searchByQueryViewModel.userActionFlow.tryEmit(UserActionSearchByQuery.OnQueryChanged(newText))
                        return false
                    }
                })
        }

    override fun renderView() {
        //CoroutineScope  привязаннный к LifecycleOwner's [Lifecycle]
        lifecycleScope
            .launchWhenStarted {
                searchByQueryViewModel
                    .searchViewState
                    .collect {
                        Timber.d("$TEST_LOG_TAG renderView SearchViewState by query: $it")
                        when (val state = it.items) {
                            is SearchViewState.Items.Loading -> showLoading()
                            is SearchViewState.Items.Drinks -> showDrinks(state)
                            is SearchViewState.Items.Error -> showError(state)
                            is SearchViewState.Items.Idle -> showIdle()
                        }
                    }
            }
    }

    override fun showLoading() {
        binding.progressBar.setVisibility(true)
        binding.infoTextView.setVisibility(false)
        binding.cocktailsRecycler.setVisibility(false)
    }

    override fun showDrinks(result: SearchViewState.Items.Drinks) {
        binding.progressBar.setVisibility(false)
        binding.infoTextView.setVisibility(false)
        binding.cocktailsRecycler.setVisibility(true)
        drinksAdapter.listCocktails = result.drinksList
    }

    override fun showError(result: SearchViewState.Items.Error) {
        binding.progressBar.setVisibility(false)
        binding.cocktailsRecycler.setVisibility(false)
        binding.infoTextView.setVisibility(true)
        binding.infoTextView.text = result.error
    }

    override fun showIdle() {
        binding.progressBar.setVisibility(false)
        binding.infoTextView.setVisibility(true)
        binding.infoTextView.text = "Input text"
        binding.cocktailsRecycler.setVisibility(false)
    }

    private fun onItemClick(item: CocktailListItem) {
        val fragment = CocktailFragment.newInstance(item.idDrink)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun onFavoriteClick(item: CocktailListItem) {
        Timber.d("search screen onFavoriteClick item: ${item.strDrink} is ${item.isFavorite}")
        searchByQueryViewModel.userActionFlow.tryEmit(UserActionSearchByQuery.OnFavoritesChanged(item))
    }

    companion object {
        fun newInstance(): SearchByQueryFragment = SearchByQueryFragment()
    }
}