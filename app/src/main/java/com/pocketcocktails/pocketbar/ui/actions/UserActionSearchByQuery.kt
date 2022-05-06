package com.pocketcocktails.pocketbar.ui.actions

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.data.room.Favorite

sealed class UserActionSearchByQuery {
    data class OnQueryChanged(val searchText: String) : UserActionSearchByQuery()
    data class OnFavoritesChanged(val favoriteId: CocktailListItem) : UserActionSearchByQuery()
}