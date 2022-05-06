package com.pocketcocktails.pocketbar.ui.viewstate

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.Result
import com.pocketcocktails.pocketbar.utils.SearchPartialViewState
import timber.log.Timber

object SearchPartialViewStates {

    fun onQueryChanged(queryText: String): SearchPartialViewState = { previousViewState ->
        Timber.d("$TEST_LOG_TAG SearchPartialViewStates onQueryChanged queryText: $queryText, previousViewState: $previousViewState")
        val previousStateCopy = previousViewState.copy(query = queryText, items = SearchViewState.Items.Loading)
        Timber.d("$TEST_LOG_TAG SearchPartialViewStates onQueryChanged previousViewState after copy: $previousStateCopy")
        previousStateCopy
    }

    fun onSearchResult(result: Result<List<CocktailListItem>>): SearchPartialViewState =
        { previousViewState ->
            Timber.d("$TEST_LOG_TAG SearchPartialViewStates onSearchResult previousViewState: $previousViewState")
            val items = when (result) {
                is Result.Success -> SearchViewState.Items.Drinks(result.data)
                is Result.Failure -> SearchViewState.Items.Error(result.exception.message)
            }
            val onSearchResult = previousViewState.copy(items = items)
            Timber.d("$TEST_LOG_TAG SearchPartialViewStates onSearchResult previousViewState after copy: $onSearchResult")
            onSearchResult
        }

    fun onFavoriteChanged(cocktail: CocktailListItem): SearchPartialViewState =
        { previousViewState ->
            val previousStateCopy = previousViewState.copy(isFavorite = cocktail.isFavorite)
            previousStateCopy
        }
}