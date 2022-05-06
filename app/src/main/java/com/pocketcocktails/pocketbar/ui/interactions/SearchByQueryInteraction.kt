package com.pocketcocktails.pocketbar.ui.interactions

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.data.repository.CocktailsRepository
import com.pocketcocktails.pocketbar.data.room.Favorite
import com.pocketcocktails.pocketbar.data.room.FavoriteDatabase
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SearchByQueryInteraction @Inject constructor(
    private val cocktailsRepository: CocktailsRepository,
    private val favoriteDatabase: FavoriteDatabase
) {

    fun searchDrink(searchText: String): Flow<Result<List<CocktailListItem>>> = flow {
        if (searchText.isEmpty()) {
            emit(value = Result.Success(emptyList<CocktailListItem>()))
        } else {
            try {
                var data = cocktailsRepository.getDrinkByName(searchText)
                data.map { Timber.d("$TEST_LOG_TAG ---- getDrinkByName ---- $it") }

                val favorites: ArrayList<Favorite> = ArrayList(favoriteDatabase.getFavoriteDAO().getAllFavorites())
                Timber.d("$TEST_LOG_TAG ----  favorites ---- $favorites")

                data = data.map { cocktail ->
                    var item = cocktail
                    favorites.map { favorite ->
                        if (cocktail.idDrink == favorite.idDrink) {
                            item = cocktail.copy(isFavorite = true)
                        }
                    }
                    item
                }
                emit(value = Result.Success(data))
            } catch (e: Throwable) {
                emit(value = Result.Failure(e))
            }
        }
    }

    suspend fun changeFavorite(cocktail: CocktailListItem) {
        val favorites: ArrayList<Favorite> = ArrayList(favoriteDatabase.getFavoriteDAO().getAllFavorites())
        val idCocktail = cocktail.idDrink

        if (cocktail.isFavorite) {
            val deletedItem = favorites.find { item -> item.idDrink == idCocktail }
            favorites.remove(deletedItem)
            val delete = Favorite(cocktail.idDrink)
            favoriteDatabase.getFavoriteDAO().delete(delete)
        } else {
            val favorite = Favorite(idDrink = idCocktail)
            favoriteDatabase.getFavoriteDAO().add(favorite)
        }
    }
}