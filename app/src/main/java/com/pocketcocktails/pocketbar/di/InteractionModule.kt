package com.pocketcocktails.pocketbar.di

import com.pocketcocktails.pocketbar.data.repository.CocktailsRepository
import com.pocketcocktails.pocketbar.data.repository.DefaultCocktailsRepository
import com.pocketcocktails.pocketbar.ui.interactions.CocktailInteraction
import com.pocketcocktails.pocketbar.ui.interactions.FavoritesInteraction
import com.pocketcocktails.pocketbar.ui.interactions.SearchByBaseInteraction
import com.pocketcocktails.pocketbar.ui.interactions.SearchByQueryInteraction
import dagger.Module
import dagger.Provides

@Module
object InteractionModule {

    @Provides
    fun provideCocktailInteraction(interactor: CocktailInteraction): CocktailInteraction =
        interactor

    @Provides
    fun provideFavoritesInteraction(interactor: FavoritesInteraction): FavoritesInteraction =
        interactor

    @Provides
    fun provideSearchByBaseInteraction(interactor: SearchByBaseInteraction): SearchByBaseInteraction =
        interactor

    @Provides
    fun provideSearchByQueryInteraction(interactor: SearchByQueryInteraction): SearchByQueryInteraction =
        interactor

}