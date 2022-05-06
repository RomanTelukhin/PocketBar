package com.pocketcocktails.pocketbar.ui.viewstate

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.FavoritesPartialViewState
import com.pocketcocktails.pocketbar.utils.Result
import timber.log.Timber

//Описываем состояние экрана
object FavoritePartialViewState {

    fun onFavoritesLoading(): FavoritesPartialViewState =
            { previousViewState ->
                val previousStateCopy = previousViewState.copy(items = FavoritesViewState.Items.Loading)
                Timber.d("$TEST_LOG_TAG onFavoritesLoading previousViewState after copy: $previousStateCopy")
                previousStateCopy
            }

    fun onFavoritesResult(result: Result<List<CocktailListItem>>): FavoritesPartialViewState =
            { previousViewState ->
                Timber.d("$TEST_LOG_TAG onFavoritesResult previousViewState: $previousViewState")
                val items = when (result) {
                    is Result.Success -> FavoritesViewState.Items.Drinks(result.data)
                    is Result.Failure -> FavoritesViewState.Items.Error(result.exception.message)
                }
                val onFavoritesResult = previousViewState.copy(items = items)
                Timber.d("$TEST_LOG_TAG onFavoritesResult previousViewState after copy: $onFavoritesResult")
                onFavoritesResult
            }

    fun onFavoriteDelete(deleteResult: Result<String>): FavoritesPartialViewState =
            { previousViewState ->
                Timber.d("$TEST_LOG_TAG onFavoriteDelete delete result: $deleteResult")
                when (deleteResult) {
                    is Result.Success -> {
                        var newViewState = previousViewState
                        if (previousViewState.items is FavoritesViewState.Items.Drinks) {
                            val items = ArrayList(previousViewState.items.drinksList)
                            val newList = items.filter { item -> item.idDrink != deleteResult.data }
                            newViewState = previousViewState.copy(items = FavoritesViewState.Items.Drinks(newList))
                        }
                        newViewState
                    }
                    else -> previousViewState
                }
            }
}