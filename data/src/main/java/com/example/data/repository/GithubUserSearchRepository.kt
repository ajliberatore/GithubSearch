package com.example.data.repository

import com.example.data.network.GithubService
import com.example.domain.model.GithubUser
import com.example.domain.repository.UserSearchRepository
import javax.inject.Inject

class GithubUserSearchRepository @Inject constructor(
    private val githubService: GithubService
) : UserSearchRepository {

    /**
     * Searches users from the given query.
     *
     * First gets the users from [GithubService.searchUsers] then enriches each
     * entry with the number of github repositories from [GithubService.userRepositories].
     *
     * Will return a list of [GithubUser]
     */
    override suspend fun searchUsers(query: String): List<GithubUser> {
        // Retrofit will handle switching the dispatcher to the IO thread here
        val searchResult = githubService.searchUsers(query)

        return searchResult.items.map {
            val githubRepositories = githubService.userRepositories(it.login)

            GithubUser(
                name = it.login,
                thumbnailUrl = it.avatarUrl,
                numRepositories = githubRepositories.size
            )
        }
    }
}
