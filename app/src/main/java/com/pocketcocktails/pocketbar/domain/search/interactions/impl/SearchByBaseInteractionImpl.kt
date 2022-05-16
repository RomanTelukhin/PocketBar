package com.pocketcocktails.pocketbar.domain.search.interactions.impl

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.domain.CocktailsRepository
import com.pocketcocktails.pocketbar.domain.search.interactions.SearchByBaseInteraction
import com.pocketcocktails.pocketbar.utils.Result
import javax.inject.Inject

class SearchByBaseInteractionImpl @Inject constructor(private val cocktailsRepository: CocktailsRepository) :
    SearchByBaseInteraction {

    override suspend fun searchDrinkByBase(searchBase: String): Result<List<CocktailListItemModel>> =
        try {
            val data = cocktailsRepository.getDrinkByBase(searchBase)
            Result.Success(data)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
}