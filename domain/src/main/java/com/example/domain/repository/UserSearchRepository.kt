package com.example.domain.repository

import com.example.domain.model.GithubUser

interface UserSearchRepository {

    suspend fun searchUsers(query: String): List<GithubUser>
}
