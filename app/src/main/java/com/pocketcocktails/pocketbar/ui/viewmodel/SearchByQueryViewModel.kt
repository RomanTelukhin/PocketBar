package com.pocketcocktails.pocketbar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.ui.actions.UserActionSearchByQuery
import com.pocketcocktails.pocketbar.ui.interactions.SearchByQueryInteraction
import com.pocketcocktails.pocketbar.ui.viewstate.SearchPartialViewStates
import com.pocketcocktails.pocketbar.ui.viewstate.SearchViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.SearchPartialViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByQueryViewModel @Inject constructor(private val searchByQueryInteraction: SearchByQueryInteraction) : ViewModel() {

    /**Нужен для обмена данными между корутинами
    Можно подключить несколько потребителей
    Распроситраняет последний элемент всем подписчикам.
    Каждый подписчик сразу получает последний отправленный элемент.*/
    val userActionFlow = MutableSharedFlow<UserActionSearchByQuery>(1)

    /**"Коллектор" состояний который слушает вью. Он отправляет состояния*/
    private val mutableStateFlow = MutableStateFlow(SearchViewState(EMPTY_STRING, false, SearchViewState.Items.Idle))
    val searchViewState: StateFlow<SearchViewState>
        get() = mutableStateFlow

    private fun performSearchByQuery(queryText: String): Flow<SearchPartialViewState> = flow {
        Timber.d("$TEST_LOG_TAG performSearchByQuery flow: $this")
        emit(value = SearchPartialViewStates.onQueryChanged(queryText = queryText))
        delay(1000L)

        val result = searchByQueryInteraction.searchDrink(queryText)
            .collect { value ->
                emit(value = SearchPartialViewStates.onSearchResult(result = value))
            }
        Timber.d("$TEST_LOG_TAG performSearchByQuery flow result: $result")
    }

    private fun onFavoriteClick(cocktail: CocktailListItem): Flow<SearchPartialViewState> = flow {
        Timber.d("$TEST_LOG_TAG onFavoriteClick flow: $this")
        emit(value = SearchPartialViewStates.onFavoriteChanged(cocktail = cocktail))
        searchByQueryInteraction.changeFavorite(cocktail)
    }

    init {
        val favoritePartialStateFlow: Flow<SearchPartialViewState> = userActionFlow
            /**Возвращает поток, содержащий только значения, которые являются экземплярами указанного типа.*/
            .filterIsInstance<UserActionSearchByQuery.OnFavoritesChanged>()
            .flatMapLatest { action ->
                Timber.d("$TEST_LOG_TAG favoritePartialStateFlow flatMapLatest: $action")
                onFavoriteClick(action.favoriteId)
            }

        val queryPartialStateFlow: Flow<SearchPartialViewState> = userActionFlow
            /**Возвращает поток, содержащий только значения, которые являются экземплярами указанного типа.*/
            .filterIsInstance<UserActionSearchByQuery.OnQueryChanged>()
            /**Переключение на новый поток для эмита частичного стейта.....*/
            .flatMapLatest { action ->
                Timber.d("$TEST_LOG_TAG queryPartialStateFlow flatMapLatest: $action")
                performSearchByQuery(queryText = action.searchText)
            }

        val allPartialStateFlow: Flow<SearchPartialViewState> = merge(queryPartialStateFlow, favoritePartialStateFlow)

        /**Проверяем Flow "подписываясь" на него*/
        allPartialStateFlow
            /**у scan() есть initial value - это дефолтный, в данном случае SearchViewState с дефолтным конструктором
            scan ожидает от вышестоящего потока частичные стейты*/
            .scan(
                /**"Под капотом" initial запоминается в переменной accumulator
                 * и эмитится ниже. Затем на вышестоящем flow вызывается collect и когда в collect приходит новое значение,
                 * вызывается operation, куда отдается значение accumulator.
                 * Лямбда operation должна вернуть новое значение, которое записывается в accumulator и эмитится ниже*/
                initial = SearchViewState(),
                operation = { searchViewState, previousViewState ->
                    Timber.d("$TEST_LOG_TAG queryPartialStateFlow scan  searchViewState: $searchViewState, previousViewState: $previousViewState")
                    return@scan previousViewState.invoke(searchViewState)
                })
            .onEach { viewState ->
                Timber.d("$TEST_LOG_TAG queryPartialStateFlow onEach: $viewState")
                mutableStateFlow.value = viewState
            }
            .launchIn(viewModelScope)
    }
}