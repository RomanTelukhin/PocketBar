package com.pocketcocktails.pocketbar.data.dto.search


import com.google.gson.annotations.SerializedName

data class DrinkList(
    @SerializedName("drinks") val cocktailsList: List<DrinkInfo>
)