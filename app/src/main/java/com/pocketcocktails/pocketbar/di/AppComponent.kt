package com.pocketcocktails.pocketbar.di

import android.app.Application
import android.content.Context
import com.pocketcocktails.pocketbar.CocktailsApp
import com.pocketcocktails.pocketbar.ui.view.CocktailFragment
import com.pocketcocktails.pocketbar.ui.view.FavoritesFragment
import com.pocketcocktails.pocketbar.ui.view.SearchByBaseFragment
import com.pocketcocktails.pocketbar.ui.view.SearchByQueryFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        AppModule::class,
        ViewModelModule::class
    ]
)
@ExperimentalCoroutinesApi
interface AppComponent {
    fun context(): Context
    fun applicationContext(): Application


    fun inject(fragment: CocktailFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: SearchByBaseFragment)
    fun inject(fragment: SearchByQueryFragment)
}