package com.pocketcocktails.pocketbar.di

import androidx.lifecycle.ViewModel
import com.pocketcocktails.pocketbar.ui.viewmodel.CocktailViewModel
import com.pocketcocktails.pocketbar.ui.viewmodel.FavoritesViewModel
import com.pocketcocktails.pocketbar.ui.viewmodel.SearchByBaseViewModel
import com.pocketcocktails.pocketbar.ui.viewmodel.SearchByQueryViewModel
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
object ViewModelModule {
    @Provides
    fun provideCocktailViewModel(viewModel: CocktailViewModel): ViewModel = viewModel
    @Provides
    fun provideFavoritesViewModel(viewModel: FavoritesViewModel): ViewModel = viewModel
    @Provides
    fun provideSearchByBaseViewModel(viewModel: SearchByBaseViewModel): ViewModel = viewModel
    @Provides
    fun provideSearchByQueryViewModel(viewModel: SearchByQueryViewModel): ViewModel = viewModel

}