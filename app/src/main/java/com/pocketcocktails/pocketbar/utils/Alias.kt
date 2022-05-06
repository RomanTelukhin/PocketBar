package com.pocketcocktails.pocketbar.utils

import com.pocketcocktails.pocketbar.ui.viewstate.CocktailViewState
import com.pocketcocktails.pocketbar.ui.viewstate.FavoritesViewState
import com.pocketcocktails.pocketbar.ui.viewstate.SearchViewState

typealias SearchPartialViewState = (SearchViewState) -> SearchViewState
typealias CocktailPartialViewState = (CocktailViewState) -> CocktailViewState
typealias FavoritesPartialViewState = (FavoritesViewState) -> FavoritesViewState