package com.pocketcocktails.pocketbar.ui.viewstate

import com.pocketcocktails.pocketbar.data.domain.CocktailInfo
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.utils.CocktailPartialViewState
import com.pocketcocktails.pocketbar.utils.Constants
import com.pocketcocktails.pocketbar.utils.Result
import timber.log.Timber

object CocktailPartialViewStates {

    fun onIdChanged(): CocktailPartialViewState = { previousViewState ->
        val previousStateCopy = previousViewState.copy(cocktail = CocktailViewState.Item.Loading)
        previousStateCopy
    }

    fun onSearchResult(result: Result<CocktailInfo>): CocktailPartialViewState =
        { previousViewState ->
            val items = when (result) {
                is Result.Success -> CocktailViewState.Item.Cocktail(result.data)
                is Result.Failure -> CocktailViewState.Item.Error(result.exception.message)
            }
            val onSearchResult = previousViewState.copy(cocktail = items)
            onSearchResult
        }

}