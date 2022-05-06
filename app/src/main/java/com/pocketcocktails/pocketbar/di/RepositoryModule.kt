package com.pocketcocktails.pocketbar.di

import com.pocketcocktails.pocketbar.data.repository.CocktailsRepository
import com.pocketcocktails.pocketbar.data.repository.DefaultCocktailsRepository
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {

    @Provides
    fun provideMainRepositoryImpl(repository: DefaultCocktailsRepository): CocktailsRepository =
        repository

}
