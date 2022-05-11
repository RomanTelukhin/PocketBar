package com.pocketcocktails.pocketbar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.ui.actions.UserActionSearchByBase
import com.pocketcocktails.pocketbar.ui.interactions.SearchByBaseInteraction
//import com.pocketcocktails.pocketbar.ui.viewstate.SearchPartialViewStates
import com.pocketcocktails.pocketbar.ui.viewstate.SearchViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.Result
//import com.pocketcocktails.pocketbar.utils.SearchPartialViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByBaseViewModel @Inject constructor(private val searchInteraction: SearchByBaseInteraction) :
    ViewModel() {
    val userActionFlow = MutableSharedFlow<UserActionSearchByBase>(1)
    private val mutableStateFlow =
        MutableStateFlow(SearchViewState(EMPTY_STRING, false, SearchViewState.Items.Idle))

    val cocktailsByBaseViewState: StateFlow<SearchViewState>
        get() = mutableStateFlow

    private fun performSearchByBase(base: String): Flow<SearchPartialViewState> = flow {
        Timber.d("$TEST_LOG_TAG performSearch flow: $this")
        emit(value = onQueryChanged(queryText = base))
        val result = searchInteraction.searchDrinkByBase(base)
        Timber.d("$TEST_LOG_TAG performSearch flow result: $result")
        emit(value = onSearchResult(result = result))
    }

    init {
        val queryPartialStateFlow: Flow<SearchPartialViewState> = userActionFlow
            .filterIsInstance<UserActionSearchByBase.OnBaseChanged>()
            .flatMapLatest { action ->
                Timber.d("$TEST_LOG_TAG queryPartialStateFlow flatMapLatest: $action")
                performSearchByBase(base = action.base)
            }

        val allPartialStateFlow: Flow<SearchPartialViewState> = merge(queryPartialStateFlow)

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

    private fun onQueryChanged(queryText: String): SearchPartialViewState = { previousViewState ->
        Timber.d("$TEST_LOG_TAG SearchPartialViewStates onQueryChanged queryText: $queryText, previousViewState: $previousViewState")
        val previousStateCopy =
            previousViewState.copy(query = queryText, items = SearchViewState.Items.Loading)
        Timber.d("$TEST_LOG_TAG SearchPartialViewStates onQueryChanged previousViewState after copy: $previousStateCopy")
        previousStateCopy
    }

    private fun onSearchResult(result: Result<List<CocktailListItem>>): SearchPartialViewState =
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

    private fun onFavoriteChanged(cocktail: CocktailListItem): SearchPartialViewState =
        { previousViewState ->
            val previousStateCopy = previousViewState.copy(isFavorite = cocktail.isFavorite)
            previousStateCopy
        }

}

typealias SearchPartialViewState = (SearchViewState) -> SearchViewState