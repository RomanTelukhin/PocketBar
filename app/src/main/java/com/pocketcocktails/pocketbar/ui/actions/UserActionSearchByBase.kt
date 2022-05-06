package com.pocketcocktails.pocketbar.ui.actions

sealed class UserActionSearchByBase {
    data class OnBaseChanged(val base: String) : UserActionSearchByBase()
    data class OnFavoritesChanged(val id: String) : UserActionSearchByBase()
}