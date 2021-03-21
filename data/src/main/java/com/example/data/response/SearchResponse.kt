package com.example.data.response

import com.squareup.moshi.Json

data class SearchResponse(
    val items: List<UserResponse>
)

data class UserResponse(
    val id: Int,
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)
