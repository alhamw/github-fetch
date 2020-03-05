package com.githubfetch.android.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("login")
    val loginName: String = "",
    @SerializedName("avatar_url")
    val avatarUrl: String = ""
)