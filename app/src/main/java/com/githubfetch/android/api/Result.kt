package com.githubfetch.android.api

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Result<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>
)