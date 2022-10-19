package com.pocketcocktails.pocketbar.domain.search.interactions

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.utils.Result

interface SearchByBaseInteraction {

    suspend fun searchDrinkByBase(searchBase: String): Result<List<CocktailListItemModel>>
    suspend fun changeFavorite(cocktail: CocktailListItemModel)
}