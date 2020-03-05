package com.githubfetch.android.data.repository

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.githubfetch.android.api.GithubService
import com.githubfetch.android.api.Result
import com.githubfetch.android.data.UserDataSourceFactory
import com.githubfetch.android.data.UserSearchDataSourceFactory
import com.githubfetch.android.data.model.User

class UserRepository(val service: GithubService) {
    private val PAGE_SIZE = 10
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(PAGE_SIZE)
        .build()

    fun getUsers(): Result<User> {
        val userDataSourceFactory = UserDataSourceFactory(service)

        return Result(
            LivePagedListBuilder(userDataSourceFactory, config).build(),
            Transformations.switchMap(userDataSourceFactory.userLiveDataSource) {
                it.networkState
            }
        )
    }

    fun searchUser(query: String): Result<User> {
        val userSearchDataSourceFactory = UserSearchDataSourceFactory(query, service)

        return Result(
            LivePagedListBuilder(userSearchDataSourceFactory, config).build(),
            Transformations.switchMap(userSearchDataSourceFactory.userSearchLiveDataSource) {
                it.networkState
            }
        )
    }
}