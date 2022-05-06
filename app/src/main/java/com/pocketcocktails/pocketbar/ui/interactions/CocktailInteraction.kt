package com.pocketcocktails.pocketbar.ui.interactions

import com.pocketcocktails.pocketbar.data.domain.CocktailInfo
import com.pocketcocktails.pocketbar.data.repository.CocktailsRepository
import com.pocketcocktails.pocketbar.utils.Constants
import com.pocketcocktails.pocketbar.utils.Result
import timber.log.Timber
import javax.inject.Inject

class CocktailInteraction @Inject constructor(private val cocktailsRepository: CocktailsRepository) {

    suspend fun getDrinkById(id: String): Result<CocktailInfo> =
        try {
            val data = cocktailsRepository.getDrinkById(id)
            Timber.d("${Constants.TEST_LOG_TAG} getDrinkById data: $data")
            Result.Success(data)
        } catch (e: Throwable) {
            Timber.d("${Constants.TEST_LOG_TAG} getDrinkById data: $e")
            Result.Failure(e)
        }
}