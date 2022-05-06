package com.pocketcocktails.pocketbar.data.domain

data class CocktailInfo(
    val idDrink: String?,
    val name: String?,
    val drinkThumb: String?,
    val drinkAlternate: String?,
    val tags: String?,
    val video: String?,
    val category: String?,
    val IBA: String?,
    val alcoholic: String?,
    val glass: String?,
    val guide: Map<String, String>?,
    val instructionsZHHANS: String?,
    val instructionsZHHANT: String?,
    val ingredient: List<Ingredient>,
    val imageSource: String?,
    val imageAttribution: String?,
    val creativeCommonsConfirmed: String?,
    val dateModified: String?
)
