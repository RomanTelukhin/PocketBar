package com.pocketcocktails.pocketbar.data.domain


data class CocktailListItem(
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val isFavorite: Boolean
)