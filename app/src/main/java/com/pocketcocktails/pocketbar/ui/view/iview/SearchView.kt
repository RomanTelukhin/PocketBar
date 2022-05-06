package com.pocketcocktails.pocketbar.ui.view.iview

import com.pocketcocktails.pocketbar.ui.viewstate.SearchViewState

interface SearchView: BaseView {
    fun showLoading()
    fun showDrinks(result: SearchViewState.Items.Drinks)
    fun showError(result: SearchViewState.Items.Error)
    fun showIdle()
}