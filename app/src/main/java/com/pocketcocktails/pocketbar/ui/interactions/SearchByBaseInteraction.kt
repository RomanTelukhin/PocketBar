package com.pocketcocktails.pocketbar.ui.interactions

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.data.repository.CocktailsRepository
import com.pocketcocktails.pocketbar.utils.Result
import javax.inject.Inject

class SearchByBaseInteraction @Inject constructor(private val cocktailsRepository: CocktailsRepository) {

    suspend fun searchDrinkByBase(searchBase: String): Result<List<CocktailListItem>> =
        try {
            val data = cocktailsRepository.getDrinkByBase(searchBase)
            Result.Success(data)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
}