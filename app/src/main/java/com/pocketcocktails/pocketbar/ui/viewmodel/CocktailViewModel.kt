package com.pocketcocktails.pocketbar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.ui.actions.UserActionSearchById
import com.pocketcocktails.pocketbar.ui.interactions.CocktailInteraction
import com.pocketcocktails.pocketbar.ui.viewstate.CocktailPartialViewStates
import com.pocketcocktails.pocketbar.ui.viewstate.CocktailViewState
import com.pocketcocktails.pocketbar.utils.Constants
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CocktailViewModel @Inject constructor(private val searchInteraction: CocktailInteraction) : ViewModel() {

    var mIdCocktail = EMPTY_STRING

    val userActionChannel = MutableSharedFlow<UserActionSearchById>(1)

    private val mutableSelectedItem = MutableStateFlow(CocktailViewState(CocktailViewState.Item.Loading))

    val cocktailViewState: StateFlow<CocktailViewState>
        get() = mutableSelectedItem

    private fun performSearchById(): Flow<CocktailPartialViewState> = flow {
        emit(value = CocktailPartialViewStates.onIdChanged())
        delay(1000L)
        val result = searchInteraction.getDrinkById(mIdCocktail)
        Timber.d("${Constants.TEST_LOG_TAG} performSearchById data: $result")
        emit(value = CocktailPartialViewStates.onSearchResult(result = result))
    }

    init {
        val idSearchPartialStateFlow: Flow<CocktailPartialViewState> = userActionChannel
            .flatMapLatest { performSearchById() }

        idSearchPartialStateFlow
            .scan(
                initial = CocktailViewState(),
                operation = { cocktailViewState, previousViewState ->
                    return@scan previousViewState.invoke(cocktailViewState)
                })
            .onEach { viewState -> mutableSelectedItem.value = viewState }
            .launchIn(viewModelScope)
    }
}

typealias CocktailPartialViewState = (CocktailViewState) -> CocktailViewState