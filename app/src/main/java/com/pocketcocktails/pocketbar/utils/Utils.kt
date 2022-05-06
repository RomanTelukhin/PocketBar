package com.pocketcocktails.pocketbar.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pocketcocktails.pocketbar.CocktailsApp
import com.pocketcocktails.pocketbar.di.AppComponent

val Context.appComponent: AppComponent
    get() = when (this) {
        is CocktailsApp -> appComponent
        else -> this.applicationContext.appComponent
    }

fun ImageView.load(imageAddress: String) {
    Glide.with(this)
        .load(imageAddress)
        .into(this)
}
