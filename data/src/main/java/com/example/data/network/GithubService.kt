package com.example.data.network

import com.example.data.response.SearchResponse
import com.example.data.response.UserRepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    /**
     * Finds users via search term provided.
     * To help avoid hitting rate limit, we will return 3 results per serch.
     */
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") searchTerm: String,
        @Query("page") pageNum: Int = 1,
        @Query("per_page") perPage: Int = 3
    ) : SearchResponse

    /**
     * Gets the repositories for the provided user
     */
    @GET("users/{user}/repos")
    suspend fun userRepositories(
       @Path("user") user: String
    ) : List<UserRepositoryResponse>
}
