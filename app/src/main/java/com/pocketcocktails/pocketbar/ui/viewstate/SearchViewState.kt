package com.pocketcocktails.pocketbar.ui.viewstate

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING

data class SearchViewState(val query: String = EMPTY_STRING, val isFavorite: Boolean = false, val items: Items = Items.Idle) {

    sealed class Items {
        object Idle : Items()
        object Loading : Items()
        data class Drinks(val drinksList: List<CocktailListItem>) : Items()
        data class Error(val error: String?) : Items()
    }
}