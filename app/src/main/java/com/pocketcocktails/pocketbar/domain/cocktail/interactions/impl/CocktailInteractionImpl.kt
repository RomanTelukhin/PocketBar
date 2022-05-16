package com.pocketcocktails.pocketbar.domain.cocktail.interactions.impl

import com.pocketcocktails.pocketbar.presentation.model.CocktailInfoModel
import com.pocketcocktails.pocketbar.domain.CocktailsRepository
import com.pocketcocktails.pocketbar.domain.cocktail.interactions.CocktailInteraction
import com.pocketcocktails.pocketbar.utils.Constants
import com.pocketcocktails.pocketbar.utils.Result
import timber.log.Timber
import javax.inject.Inject

class CocktailInteractionImpl @Inject constructor(private val cocktailsRepository: CocktailsRepository) :
    CocktailInteraction {

    override suspend fun getDrinkById(id: String): Result<CocktailInfoModel> =
        try {
            val data = cocktailsRepository.getDrinkById(id)
            Timber.d("${Constants.TEST_LOG_TAG} getDrinkById data: $data")
            Result.Success(data)
        } catch (e: Throwable) {
            Timber.d("${Constants.TEST_LOG_TAG} getDrinkById data: $e")
            Result.Failure(e)
        }
}