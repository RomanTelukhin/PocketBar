package com.pocketcocktails.pocketbar.ui.viewstate

import com.pocketcocktails.pocketbar.data.domain.CocktailInfo
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem

data class CocktailViewState(val cocktail: Item = Item.Loading) {

    sealed class Item {
        object Loading : Item()
        data class Cocktail(val cocktailItem: CocktailInfo) : Item()
        data class Error(val error: String?) : Item()
    }
}