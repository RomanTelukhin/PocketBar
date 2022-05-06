package com.pocketcocktails.pocketbar.data.api

import com.pocketcocktails.pocketbar.data.dto.search.DrinkList
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search.php?")
    suspend fun getDrinkByName(@Query("s") searchName: String): DrinkList

    @GET("lookup.php?")
    suspend fun getDrinkById(@Query("i") searchId: String): DrinkList

    @GET("filter.php?")
    suspend fun getDrinkByIngredient(@Query("i") searchIngredient: String): DrinkList

    @GET("lookup.php?")
    suspend fun getTenRandomDrinks(@Query("i") id: String): DrinkList

}