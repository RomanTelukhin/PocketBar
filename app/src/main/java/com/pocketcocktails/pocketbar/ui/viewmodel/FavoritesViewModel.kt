package com.pocketcocktails.pocketbar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.ui.actions.UserActionShowFavorites
import com.pocketcocktails.pocketbar.ui.interactions.FavoritesInteraction
import com.pocketcocktails.pocketbar.ui.viewstate.FavoritesViewState
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FavoritesViewModel @Inject constructor(private val searchInteraction: FavoritesInteraction) : ViewModel() {
    val userActionChannel = MutableSharedFlow<UserActionShowFavorites>(1)

    private val mutableStateFlow = MutableStateFlow(FavoritesViewState(FavoritesViewState.Items.Idle))

    val favoriteCocktailsViewState: SharedFlow<FavoritesViewState>
        get() = mutableStateFlow

    private fun performShowFavorites(): Flow<FavoritesPartialViewState> = flow {
        emit(value = onFavoritesLoading())
        val result = searchInteraction.showFavoritesDrink()
        emit(value = onFavoritesResult(result = result))
        Timber.d("$TEST_LOG_TAG performSearch flow result: $result")
    }

    private fun onFavoriteClick(cocktail: CocktailListItem): Flow<FavoritesPartialViewState> = flow {
        val result = searchInteraction.deleteFavorite(cocktail)
        Timber.d("$TEST_LOG_TAG queryPartialStateFlow flatMapLatest: $result")
        emit(value = onFavoriteDelete(deleteResult = result))
    }

    init {
        val favoritesPartialStateFlow: Flow<FavoritesPartialViewState> = userActionChannel
                .filterIsInstance<UserActionShowFavorites.ShowFavorites>()
                .flatMapLatest { action ->
                    Timber.d("$TEST_LOG_TAG queryPartialStateFlow flatMapLatest: $action")
                    performShowFavorites()
                }

        val changeFavoritePartialStateFlow: Flow<FavoritesPartialViewState> = userActionChannel
                /**Певращает в "горячий" поток
                 * То есть генерируют данные сразу при создании, даже если нет получателей(подписок)*/
                /**Возвращает поток, содержащий только значения, которые являются экземплярами указанного типа.*/
                .filterIsInstance<UserActionShowFavorites.OnFavoritesChanged>()
                .flatMapLatest { action ->
                    Timber.d("$TEST_LOG_TAG queryPartialStateFlow flatMapLatest: $action")
                    Timber.d("$TEST_LOG_TAG queryPartialStateFlow flatMapLatest: id ${action.favoriteId}")
                    onFavoriteClick(action.favoriteId)
                }

        val allPartialStateFlow: Flow<FavoritesPartialViewState> = merge(favoritesPartialStateFlow, changeFavoritePartialStateFlow)

        allPartialStateFlow
                .scan(
                        initial = FavoritesViewState(),
                        operation = { favoritesViewState, previousViewState ->
                            Timber.d("$TEST_LOG_TAG favoritesPartialStateFlow scan favoritesViewState: $favoritesViewState, previousViewState: $previousViewState")
                            return@scan previousViewState.invoke(favoritesViewState)
                        })
                .onEach { viewState ->
                    Timber.d("$TEST_LOG_TAG favoritesPartialStateFlow onEach: ${viewState.items}")
                    Timber.d("$TEST_LOG_TAG favoritesPartialStateFlow onEach: ${mutableStateFlow.value == viewState}")
                    mutableStateFlow.value = viewState
                }
                .launchIn(viewModelScope)
    }

    private fun onFavoritesLoading(): FavoritesPartialViewState =
        { previousViewState ->
            val previousStateCopy = previousViewState.copy(items = FavoritesViewState.Items.Loading)
            Timber.d("$TEST_LOG_TAG onFavoritesLoading previousViewState after copy: $previousStateCopy")
            previousStateCopy
        }

    private fun onFavoritesResult(result: Result<List<CocktailListItem>>): FavoritesPartialViewState =
        { previousViewState ->
            Timber.d("$TEST_LOG_TAG onFavoritesResult previousViewState: $previousViewState")
            val items = when (result) {
                is Result.Success -> FavoritesViewState.Items.Drinks(result.data)
                is Result.Failure -> FavoritesViewState.Items.Error(result.exception.message)
            }
            val onFavoritesResult = previousViewState.copy(items = items)
            Timber.d("$TEST_LOG_TAG onFavoritesResult previousViewState after copy: $onFavoritesResult")
            onFavoritesResult
        }

    private fun onFavoriteDelete(deleteResult: Result<String>): FavoritesPartialViewState =
        { previousViewState ->
            Timber.d("$TEST_LOG_TAG onFavoriteDelete delete result: $deleteResult")
            when (deleteResult) {
                is Result.Success -> {
                    var newViewState = previousViewState
                    if (previousViewState.items is FavoritesViewState.Items.Drinks) {
                        val items = ArrayList(previousViewState.items.drinksList)
                        val newList = items.filter { item -> item.idDrink != deleteResult.data }
                        newViewState = previousViewState.copy(items = FavoritesViewState.Items.Drinks(newList))
                    }
                    newViewState
                }
                else -> previousViewState
            }
        }
}

typealias FavoritesPartialViewState = (FavoritesViewState) -> FavoritesViewState