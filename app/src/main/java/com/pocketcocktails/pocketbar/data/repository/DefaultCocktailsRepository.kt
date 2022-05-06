package com.pocketcocktails.pocketbar.data.repository

import com.pocketcocktails.pocketbar.data.api.ApiService
import com.pocketcocktails.pocketbar.data.domain.CocktailInfo
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.data.domain.Ingredient
import java.io.IOException
import javax.inject.Inject

class DefaultCocktailsRepository @Inject constructor(
    private val apiHelper: ApiService,
) : CocktailsRepository {

    @Throws(IOException::class)
    override suspend fun getDrinkByName(searchText: String): List<CocktailListItem> {
        val cocktailsList = apiHelper.getDrinkByName(searchText)

        return cocktailsList.cocktailsList.map { cocktail ->
            CocktailListItem(
                cocktail.idDrink,
                cocktail.strDrink,
                cocktail.strDrinkThumb,
                false
            )
        }.toList()
    }

    @Throws(IOException::class)
    override suspend fun getFavoriteById(id: String): CocktailListItem {
        val favoriteItemList = apiHelper.getDrinkById(id).cocktailsList.first()
        return CocktailListItem(
            favoriteItemList.idDrink,
            favoriteItemList.strDrink,
            favoriteItemList.strDrinkThumb,
            true
        )
    }

    override suspend fun getDrinkById(id: String): CocktailInfo {
        val drink = apiHelper.getDrinkById(id).cocktailsList.first()

        val ingredients = arrayListOf<Ingredient>()
        var ingrid = Ingredient()

        if (drink.strIngredient1.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient1)
            if (drink.strMeasure1.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure1)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient2.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient2)
            if (drink.strMeasure2.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure2)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient3.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient3)
            if (drink.strMeasure3.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure3)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient4.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient4)
            if (drink.strMeasure4.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure4)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient5.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient5)
            if (drink.strMeasure5.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure5)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient6.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient6)
            if (drink.strMeasure6.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure6)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient7.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient7)
            if (drink.strMeasure7.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure7)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient8.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient8)
            if (drink.strMeasure8.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure8)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient9.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient9)
            if (drink.strMeasure9.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure9)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient10.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient10)
            if (drink.strMeasure10.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure10)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient11.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient11)
            if (drink.strMeasure11.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure11)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient12.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient12)
            if (drink.strMeasure12.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure12)
                ingredients.add(ingrid)
            }
        }

        if (drink.strIngredient13.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient13)
            if (drink.strMeasure13.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure13)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient14.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient14)
            if (drink.strMeasure14.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure14)
            }
            ingredients.add(ingrid)
        }

        if (drink.strIngredient15.isNotEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient15)
            if (drink.strMeasure15.isNotEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure15)
            }
            ingredients.add(ingrid)
        }

        val instructions = arrayListOf<String>()
        instructions.add(drink.strInstructions)
        instructions.add(drink.strInstructionsDE)
        instructions.add(drink.strInstructionsES)
        instructions.add(drink.strInstructionsFR)
        instructions.add(drink.strInstructionsIT)

        val locale = arrayListOf<String>()
        locale.add("EN")
        locale.add("DE")
        locale.add("ES")
        locale.add("FR")
        locale.add("IT")

        val guide: Map<String, String> = locale.zip(instructions).toMap()

        return CocktailInfo(
            idDrink = drink.idDrink,
            name = drink.strDrink,
            drinkThumb = drink.strDrinkThumb,
            drinkAlternate = drink.strDrinkAlternate,
            tags = drink.strTags,
            video = drink.strVideo,
            category = drink.strCategory,
            IBA = drink.strIBA,
            alcoholic = drink.strAlcoholic,
            glass = drink.strGlass,
            guide = guide,
            instructionsZHHANS = drink.strInstructionsZHHANS,
            instructionsZHHANT = drink.strInstructionsZHHANT,
            ingredient = ingredients,
            imageSource = drink.strImageSource,
            imageAttribution = drink.strImageAttribution,
            creativeCommonsConfirmed = drink.strCreativeCommonsConfirmed,
            dateModified = drink.dateModified,
        )
    }

    override suspend fun getDrinkByBase(baseName: String): List<CocktailListItem> {
        val cocktailsList = apiHelper.getDrinkByIngredient(baseName)
        return cocktailsList.cocktailsList.map { cocktail ->
            CocktailListItem(
                cocktail.idDrink,
                cocktail.strDrink,
                cocktail.strDrinkThumb,
                false
            )
        }.toList()
    }
}