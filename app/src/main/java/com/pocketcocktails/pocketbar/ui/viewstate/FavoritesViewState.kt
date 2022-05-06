package com.pocketcocktails.pocketbar.ui.viewstate

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem

data class FavoritesViewState(val items: Items = Items.Idle, val isFavorite: Boolean = false) {

    sealed class Items {
        object Idle : Items()
        object Loading : Items()
        data class Drinks(val drinksList: List<CocktailListItem>) : Items()
        data class Error(val error: String?) : Items()
    }
}