package com.pocketcocktails.pocketbar.data.repository

import com.pocketcocktails.pocketbar.data.domain.CocktailInfo
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem

interface CocktailsRepository {
    suspend fun getDrinkByName(searchText: String): List<CocktailListItem>
    suspend fun getDrinkById(id: String): CocktailInfo
    suspend fun getDrinkByBase(baseName: String): List<CocktailListItem>
    suspend fun getFavoriteById(id: String): CocktailListItem
}