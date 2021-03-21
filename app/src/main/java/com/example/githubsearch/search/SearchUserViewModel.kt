package com.example.githubsearch.search

import android.util.Log
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.lifecycle.* // TODO update preferences not to wildcard import
import com.example.domain.model.GithubUser
import com.example.domain.repository.UserSearchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * SearchUserViewModel handles logic surrounding SearchUserFragment, and manages its state.
 * Note: [UserSearchRepository] is a dependency. If this was a more complex application, we would introduce
 * a specific use case for each responsibility, but since our repository only searches, we will omit that
 * layer from this project to avoid redundancy.
 */
@ExperimentalCoroutinesApi
@FlowPreview
class SearchUserViewModel(
    private val searchRepository: UserSearchRepository
) : ViewModel() {

    private companion object {
        const val TAG = "SearchUserViewModel"
    }

    /**
     * LiveData to handle progress bar state
     */
    private val _progressVisible = MutableLiveData(false)
    val progressVisible: LiveData<Boolean>
        get() = _progressVisible

    /**
     * LiveData used to show no results message or the list of results
     */
    val emptyList = MutableLiveData(true).switchMap {
        Transformations.map(searchResults) { it.isNullOrEmpty() }
    }

    /**
     * LiveData containing list of results from the search
     */
    private val _searchResult = MutableLiveData<List<GithubUser>>()
    val searchResults: LiveData<List<GithubUser>>
        get() = _searchResult

    /**
     * onTextChanged listens on updates in search query
     */
    val onTextChanged =
        TextViewBindingAdapter.OnTextChanged { s: CharSequence?, _: Int, _: Int, _: Int ->
            textChangedChannel.offer(s)
        }


    private val textChangedChannel = BroadcastChannel<CharSequence?>(Channel.CONFLATED)
    private var searchJob: Job? = null

    init {
        /**
         * Listen on text changes from channel.
         * debounce to reduce unnecessary searches, and only search if > 3 chars when auto searching.
         */
        viewModelScope.launch {
            textChangedChannel.asFlow()
                .debounce(500)
                .filter { it?.length ?: 0 >= 3 }
                .collect {
                    it?.let { query ->
                        searchUser(query.toString())
                    }
                }
        }
    }

    /**
     * Fetches users from the repository using the query provided
     */
    fun searchUser(query: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _progressVisible.value = false
            _searchResult.value = emptyList()
            Log.e(TAG, "Error getting search results", throwable)
        }

        searchJob?.cancel() // Ensure we cancel any outstanding job
        searchJob = viewModelScope.launch(exceptionHandler) {
            _progressVisible.value = true
            _searchResult.value = searchRepository.searchUsers(query)
            _progressVisible.value = false
        }
    }

    /**
     * Factor class to help ViewModelProvider instantiate this ViewModel with its dependency
     */
    internal class Factory @Inject constructor(
        private val searchRepository: UserSearchRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchUserViewModel::class.java)) {
                return SearchUserViewModel(searchRepository) as T
            } else {
                throw IllegalAccessException("Expecting SearchUserViewModel")
            }
        }
    }
}

