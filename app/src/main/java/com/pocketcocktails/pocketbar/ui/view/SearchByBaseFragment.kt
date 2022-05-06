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
import com.pocketcocktails.pocketbar.databinding.FragmentCocktailByBaseBinding
import com.pocketcocktails.pocketbar.ui.actions.UserActionSearchByBase
import com.pocketcocktails.pocketbar.ui.adapter.DrinksAdapter
import com.pocketcocktails.pocketbar.ui.viewmodel.SearchByBaseViewModel
import com.pocketcocktails.pocketbar.ui.viewstate.SearchViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.appComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByBaseFragment : Fragment() {

    private lateinit var binding: FragmentCocktailByBaseBinding
    private lateinit var drinksAdapter: DrinksAdapter
    private var cocktailBase = EMPTY_STRING

    @Inject
    lateinit var searchByBaseViewModel: SearchByBaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCocktailByBaseBinding.inflate(inflater, container, false)
        requireContext().appComponent.inject(this)
        renderView()
        setupView()
        return binding.root
    }

    private fun setupView() {
        searchByBaseViewModel.userActionFlow.tryEmit(UserActionSearchByBase.OnBaseChanged(cocktailBase))
        drinksAdapter = DrinksAdapter(
                onItemClick = { cocktailListItem -> onItemClick(cocktailListItem) },
                onFavoriteClick = { cocktailListItem -> onFavoriteClick(cocktailListItem) }
        )
        with(receiver = binding) {
            cocktailsRecycler.layoutManager = LinearLayoutManager(requireContext())
            cocktailsRecycler.adapter = drinksAdapter
        }
    }

    private fun renderView() {
        lifecycleScope
                .launchWhenStarted {
                    searchByBaseViewModel
                            .cocktailsByBaseViewState
                            .collect {
                                Timber.d("$TEST_LOG_TAG renderView SearchViewState by base: $it")
                                when (val state = it.items) {
                                    is SearchViewState.Items.Loading -> showLoading()
                                    is SearchViewState.Items.Drinks -> showDrinks(state)
                                    is SearchViewState.Items.Error -> showError(state)
                                }
                            }
                }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cocktailsRecycler.visibility = View.GONE
    }

    private fun showDrinks(result: SearchViewState.Items.Drinks) {
        Timber.d("$TEST_LOG_TAG renderView SearchViewState: $")
        binding.progressBar.visibility = View.GONE
        binding.cocktailsRecycler.visibility = View.VISIBLE
        drinksAdapter.listCocktails = result.drinksList
    }

    private fun showError(result: SearchViewState.Items.Error) {
        binding.progressBar.visibility = View.GONE
        binding.cocktailsRecycler.visibility = View.GONE
    }

    private fun onItemClick(item: CocktailListItem) {
        Timber.d("search by base screen onItemClick item: ${item.strDrink}")
        val fragment = CocktailFragment.newInstance(item.idDrink)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun onFavoriteClick(item: CocktailListItem) {
        searchByBaseViewModel.userActionFlow.tryEmit(UserActionSearchByBase.OnFavoritesChanged(item.idDrink))
    }

    companion object {
        fun newInstance(cocktailBase: String): SearchByBaseFragment {
            val fragment = SearchByBaseFragment()
            fragment.cocktailBase = cocktailBase
            return fragment
        }
    }
}