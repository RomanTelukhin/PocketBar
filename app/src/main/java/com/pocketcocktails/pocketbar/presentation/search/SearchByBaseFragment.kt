package com.pocketcocktails.pocketbar.presentation.search

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.databinding.FragmentCocktailByBaseBinding
import com.pocketcocktails.pocketbar.presentation.search.action.UserActionSearchByBase
import com.pocketcocktails.pocketbar.presentation.search.adapter.SearchAdapter
import com.pocketcocktails.pocketbar.presentation.base.BaseFragment
import com.pocketcocktails.pocketbar.presentation.cocktail.CocktailFragment
import com.pocketcocktails.pocketbar.presentation.search.viewmodel.SearchByBaseViewModel
import com.pocketcocktails.pocketbar.presentation.search.state.SearchViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.appComponent
import com.pocketcocktails.pocketbar.utils.setVisibility
import com.pocketcocktails.pocketbar.utils.showToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByBaseFragment : BaseFragment<FragmentCocktailByBaseBinding>() {

    private lateinit var drinksAdapter: SearchAdapter

    private var cocktailBase = EMPTY_STRING

    override fun getViewBinding(): FragmentCocktailByBaseBinding =
        FragmentCocktailByBaseBinding.inflate(layoutInflater)

    override fun injectViewModel(appContext: Context) {
        appContext.appComponent.inject(this)
    }

    @Inject
    lateinit var searchByBaseViewModel: SearchByBaseViewModel

    override fun setupView() {
        searchByBaseViewModel.userActionFlow.tryEmit(
            UserActionSearchByBase.OnBaseChanged(cocktailBase)
        )

        drinksAdapter = SearchAdapter(
            onItemClick = { cocktailListItem -> onItemClick(cocktailListItem) },
            onFavoriteClick = { cocktailListItem -> onFavoriteClick(cocktailListItem) }
        )

        with(receiver = binding) {
            cocktailsRecycler.layoutManager = LinearLayoutManager(requireContext())
            cocktailsRecycler.adapter = drinksAdapter
        }
    }

    override fun renderView() {
        searchByBaseViewModel.cocktailsByBaseViewState
            .flowWithLifecycle(lifecycle = lifecycle, minActiveState = Lifecycle.State.STARTED)
            .onEach { viewState ->
                when (val state = viewState.items) {
                    is SearchViewState.Items.Loading -> showLoading()
                    is SearchViewState.Items.Drinks -> showDrinks(state)
                    is SearchViewState.Items.Error -> showError(state)
                    is SearchViewState.Items.Idle -> {}
                }
            }
            .launchIn(scope = lifecycleScope)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cocktailsRecycler.setVisibility(false)
    }

    private fun showDrinks(result: SearchViewState.Items.Drinks) {
        Timber.d("$TEST_LOG_TAG renderView SearchViewState: $")
        binding.progressBar.setVisibility(false)
        binding.cocktailsRecycler.setVisibility(true)
        drinksAdapter.listCocktails = result.drinksList
    }

    private fun showError(result: SearchViewState.Items.Error) {
        binding.progressBar.setVisibility(false)
        binding.cocktailsRecycler.setVisibility(false)
        requireActivity().showToast(result.error ?: "Error")
    }

    private fun onItemClick(item: CocktailListItemModel) {
        Timber.d("search by base screen onItemClick item: ${item.strDrink}")
        val fragment = CocktailFragment.newInstance(item.idDrink)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun onFavoriteClick(item: CocktailListItemModel) {
        searchByBaseViewModel.userActionFlow.tryEmit(UserActionSearchByBase.OnFavoritesChanged(item))
    }

    companion object {
        fun newInstance(cocktailBase: String): SearchByBaseFragment {
            val fragment = SearchByBaseFragment()
            fragment.cocktailBase = cocktailBase
            return fragment
        }
    }
}