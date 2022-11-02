package com.pocketcocktails.pocketbar.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.databinding.FragmentCocktailByBaseBinding
import com.pocketcocktails.pocketbar.presentation.search.action.UserActionSearchByBase
import com.pocketcocktails.pocketbar.presentation.search.adapter.SearchByBaseAdapter
import com.pocketcocktails.pocketbar.presentation.base.BaseFragment
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
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByBaseFragment : BaseFragment<FragmentCocktailByBaseBinding>() {

    private lateinit var drinksAdapter: SearchByBaseAdapter

    private val args: SearchByBaseFragmentArgs by navArgs()

    override fun getViewBinding(): FragmentCocktailByBaseBinding = FragmentCocktailByBaseBinding.inflate(layoutInflater)

    override fun injectViewModel(appContext: Context) {
        appContext.appComponent.inject(this)
    }

    @Inject
    lateinit var searchByBaseViewModel: SearchByBaseViewModel

    override fun setupView() {
        searchByBaseViewModel.searchByBase(args.base)

        drinksAdapter = SearchByBaseAdapter(
            onFavoriteClick = { cocktailListItem -> onFavoriteClick(cocktailListItem) }
        )

        with(receiver = binding) {
            cocktailsRecycler.layoutManager = LinearLayoutManager(requireContext())
            cocktailsRecycler.adapter = drinksAdapter
        }


//        val serverRequestCollector = ServerRequestCollector()
//        val readFileCollector = ReadFileCollector()
//        val hardcodeCollector = HardcodeCollector()
//        val differentCollector = DifferentCollector(
//            serverRequestCollector,
//            readFileCollector,
//            hardcodeCollector
//        )
        val photoRecognizer = PhotoRecognizer()
        val collectorsJob = viewLifecycleOwner.lifecycleScope.launch {
//            Timber.d("Test -----: START")
//            Timber.d("Test -----: ${differentCollector.collect()}")

            Timber.d("Test -----: START")
            photoRecognizer.processPhoto()
            Timber.d("Test -----: FINISH")

        }
//        collectorsJob.cancel()
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
        drinksAdapter.differ.submitList(result.drinksList)
//        drinksAdapter.listCocktails = result.drinksList
    }

    private fun showError(result: SearchViewState.Items.Error) {
        binding.progressBar.setVisibility(false)
        binding.cocktailsRecycler.setVisibility(false)
        requireActivity().showToast(result.error ?: "Error")
    }

    private fun onFavoriteClick(item: CocktailListItemModel) {
        searchByBaseViewModel.addToFavorite(item)
    }
}