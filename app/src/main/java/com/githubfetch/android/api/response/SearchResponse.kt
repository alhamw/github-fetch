package com.githubfetch.android.api.response

import com.githubfetch.android.data.model.User
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("total_count")
    val totalCount: Int = 0,
    @SerializedName("incomplete_results")
    val incompleteResult: Boolean = false,
    @SerializedName("items")
    val users: List<User> = emptyList()
)