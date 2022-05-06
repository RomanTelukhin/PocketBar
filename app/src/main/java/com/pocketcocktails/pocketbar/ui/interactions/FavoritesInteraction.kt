package com.pocketcocktails.pocketbar.ui.interactions

import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.data.repository.CocktailsRepository
import com.pocketcocktails.pocketbar.data.room.Favorite
import com.pocketcocktails.pocketbar.data.room.FavoriteDatabase
import com.pocketcocktails.pocketbar.utils.Constants
import com.pocketcocktails.pocketbar.utils.Result
import timber.log.Timber
import javax.inject.Inject

class FavoritesInteraction @Inject constructor(
    private val cocktailsRepository: CocktailsRepository,
    private val favoriteDatabase: FavoriteDatabase
) {

    suspend fun showFavoritesDrink(): Result<List<CocktailListItem>> {
        val favorites = ArrayList<CocktailListItem>()
        return try {
            val data = favoriteDatabase.getFavoriteDAO().getAllFavorites()
            data.forEach {
                val item = cocktailsRepository.getFavoriteById(id = it.idDrink)
                favorites.add(item)
            }
            Result.Success(favorites)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }

    suspend fun deleteFavorite(cocktail: CocktailListItem): Result<String>{
        return try {
            val itemForDelete = Favorite(cocktail.idDrink)
            favoriteDatabase.getFavoriteDAO().delete(itemForDelete)
            Result.Success(cocktail.idDrink)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}
