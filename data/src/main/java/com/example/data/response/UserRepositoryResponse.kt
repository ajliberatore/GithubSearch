package com.example.data.response

/**
 * UserRepositoryResponse is a response object for data from `users/{user}}/repos`
 * We are only concerned with the quantity, so the field
 */
data class UserRepositoryResponse(
    val name: String
)
