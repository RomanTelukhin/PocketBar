package com.pocketcocktails.pocketbar.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.presentation.search.action.UserActionSearchByBase
import com.pocketcocktails.pocketbar.domain.search.interactions.SearchByBaseInteraction
import com.pocketcocktails.pocketbar.presentation.search.action.UserActionSearchByQuery
import com.pocketcocktails.pocketbar.presentation.search.state.SearchViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByBaseViewModel @Inject constructor(private val searchBybBaseInteraction: SearchByBaseInteraction) : ViewModel() {

    private val userActionFlow = MutableSharedFlow<UserActionSearchByBase>(1)

    private val mutableStateFlow = MutableStateFlow(SearchViewState(EMPTY_STRING, false, SearchViewState.Items.Idle))

    val cocktailsByBaseViewState: StateFlow<SearchViewState>
        get() = mutableStateFlow

    private fun performSearchByBase(base: String): Flow<SearchPartialViewState> = flow {
        Timber.d("$TEST_LOG_TAG performSearch flow: $this")
        emit(value = onBaseChanged(base = base))
        val result = searchBybBaseInteraction.searchDrinkByBase(base)
        Timber.d("$TEST_LOG_TAG performSearch flow result: $result")
        emit(value = onSearchResult(result = result))
    }

    private fun onFavoriteClick(cocktail: CocktailListItemModel): Flow<SearchPartialViewState> = flow {
        Timber.d("$TEST_LOG_TAG onFavoriteClick flow: $this")
        emit(value = onFavoriteChanged(cocktail = cocktail))
        searchBybBaseInteraction.changeFavorite(cocktail)
    }

    init {
        val basePartialStateFlow: Flow<SearchPartialViewState> = userActionFlow
            .filterIsInstance<UserActionSearchByBase.OnBaseChanged>()
            .distinctUntilChanged()
            .flatMapLatest { action ->
                Timber.d("$TEST_LOG_TAG queryPartialStateFlow flatMapLatest: $action")
                performSearchByBase(base = action.base)
            }

        val favoritePartialStateFlow: Flow<SearchPartialViewState> = userActionFlow
            /**Возвращает поток, содержащий только значения, которые являются экземплярами указанного типа.*/
            .filterIsInstance<UserActionSearchByBase.OnFavoritesChanged>()
            .flatMapLatest { action ->
                Timber.d("$TEST_LOG_TAG favoritePartialStateFlow flatMapLatest: $action")
                onFavoriteClick(action.favoriteId)
            }

        val allPartialStateFlow: Flow<SearchPartialViewState> = merge(basePartialStateFlow, favoritePartialStateFlow)

        allPartialStateFlow
            .scan(
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

    private fun onBaseChanged(base: String): SearchPartialViewState = { previousViewState ->
        Timber.d("$TEST_LOG_TAG SearchPartialViewStates onQueryChanged queryText: $base, previousViewState: $previousViewState")
        val previousStateCopy = previousViewState.copy(query = base, items = SearchViewState.Items.Loading)
        Timber.d("$TEST_LOG_TAG SearchPartialViewStates onQueryChanged previousViewState after copy: $previousStateCopy")
        previousStateCopy
    }

    private fun onSearchResult(result: Result<List<CocktailListItemModel>>): SearchPartialViewState =
        { previousViewState ->
            Timber.d("$TEST_LOG_TAG SearchPartialViewStates onSearchResult previousViewState: $previousViewState")
            val items = when (result) {
                is Result.Success -> SearchViewState.Items.Drinks(result.data)
                is Result.Failure -> SearchViewState.Items.Error(result.exception.message)
            }
            val onSearchResult = previousViewState.copy(items = items)
            Timber.d("$TEST_LOG_TAG SearchPartialViewStates onSearchResult previousViewState after copy: $onSearchResult")
            onSearchResult
        }

    private fun onFavoriteChanged(cocktail: CocktailListItemModel): SearchPartialViewState =
        { previousViewState ->
            val previousStateCopy = previousViewState.copy(isFavorite = cocktail.isFavorite)
            previousStateCopy
        }

    fun searchByBase(base: String) {
        userActionFlow.tryEmit(UserActionSearchByBase.OnBaseChanged(base))
    }

    fun addToFavorite(item: CocktailListItemModel) {
        userActionFlow.tryEmit(UserActionSearchByBase.OnFavoritesChanged(item))
    }

}

typealias SearchPartialViewState = (SearchViewState) -> SearchViewState