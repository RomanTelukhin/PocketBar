package com.pocketcocktails.pocketbar.data.domain

import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING

data class Ingredient(
    val name: String= EMPTY_STRING ,
    val measure: String = EMPTY_STRING ,
)