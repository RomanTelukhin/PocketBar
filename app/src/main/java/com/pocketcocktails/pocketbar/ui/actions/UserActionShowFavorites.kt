package com.pocketcocktails.pocketbar.ui.actions

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem

sealed class UserActionShowFavorites {
    object ShowFavorites : UserActionShowFavorites()
    data class OnFavoritesChanged(val favoriteId: CocktailListItem) : UserActionShowFavorites()
}