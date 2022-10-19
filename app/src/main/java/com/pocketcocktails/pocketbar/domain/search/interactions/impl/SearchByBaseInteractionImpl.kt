package com.pocketcocktails.pocketbar.domain.search.interactions.impl

import com.pocketcocktails.pocketbar.data.room.Favorite
import com.pocketcocktails.pocketbar.data.room.FavoriteDatabase
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.domain.CocktailsRepository
import com.pocketcocktails.pocketbar.domain.search.interactions.SearchByBaseInteraction
import com.pocketcocktails.pocketbar.utils.Result
import javax.inject.Inject

class SearchByBaseInteractionImpl @Inject constructor(
    private val cocktailsRepository: CocktailsRepository,
    private val favoriteDatabase: FavoriteDatabase
) : SearchByBaseInteraction {

    override suspend fun searchDrinkByBase(searchBase: String): Result<List<CocktailListItemModel>> =
        try {
            val data = cocktailsRepository.getDrinkByBase(searchBase)
            Result.Success(data)
        } catch (e: Throwable) {
            Result.Failure(e)
        }

    override suspend fun changeFavorite(cocktail: CocktailListItemModel) {
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