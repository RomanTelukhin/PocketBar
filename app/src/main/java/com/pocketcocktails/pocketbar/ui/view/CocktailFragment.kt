package com.pocketcocktails.pocketbar.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.CocktailsApp
import com.pocketcocktails.pocketbar.databinding.FragmentCocktailBinding
import com.pocketcocktails.pocketbar.di.AppComponent
import com.pocketcocktails.pocketbar.ui.actions.UserActionSearchById
import com.pocketcocktails.pocketbar.ui.adapter.IngredientsAdapter
import com.pocketcocktails.pocketbar.ui.view.iview.CocktailView
import com.pocketcocktails.pocketbar.ui.viewmodel.CocktailViewModel
import com.pocketcocktails.pocketbar.ui.viewstate.CocktailViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.appComponent
import com.pocketcocktails.pocketbar.utils.load
import com.pocketcocktails.pocketbar.utils.setVisibility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CocktailFragment : Fragment(), CocktailView {

    private lateinit var binding: FragmentCocktailBinding

    private var ingredientAdapter = IngredientsAdapter()
    
    private var idCocktail = EMPTY_STRING

    @Inject
    lateinit var cocktailViewModel: CocktailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCocktailBinding.inflate(inflater, container, false)
        requireContext().appComponent.inject(this)
        setupViewModel()
        setupView()
        renderView()
        return binding.root
    }

    private fun setupViewModel() {
        cocktailViewModel.mIdCocktail = idCocktail
        Timber.d("$TEST_LOG_TAG setupViewModel id cocktail for load is: $idCocktail")
        cocktailViewModel.userActionChannel.tryEmit(UserActionSearchById.OnIdChanged)
    }

    override fun setupView() = with(receiver = binding) {
        ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
        ingredientsRecycler.adapter = ingredientAdapter
    }

    override fun renderView() {
        lifecycleScope
            .launchWhenStarted {
                cocktailViewModel
                    .cocktailViewState
                    .collect { cocktailViewState -> showData(cocktailViewState.cocktail) }
            }
    }

    private fun showData(viewState: CocktailViewState.Item) {
        when (viewState) {
            is CocktailViewState.Item.Loading -> binding.cocktailProgress.setVisibility(true)
            is CocktailViewState.Item.Cocktail -> {
                Timber.d("$TEST_LOG_TAG cocktail item: ${viewState.cocktailItem}")
                binding.cocktailProgress.setVisibility(false)
                if (viewState.cocktailItem.drinkThumb != null) {
                    binding.nameCocktail.text = viewState.cocktailItem.name
                    binding.drinkImage.load(viewState.cocktailItem.drinkThumb)
                    Timber.d("$TEST_LOG_TAG cocktail itemingredient: ${viewState.cocktailItem}")
                    ingredientAdapter.listIngrid = viewState.cocktailItem.ingredient
                }
            }
            is CocktailViewState.Item.Error -> {
                Timber.d("$TEST_LOG_TAG error: ${viewState.error}")
                Toast.makeText(requireActivity(), "${viewState.error}", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        fun newInstance(idDrink: String): CocktailFragment {
            val fragment = CocktailFragment()
            fragment.idCocktail = idDrink
            return fragment
        }
    }
}