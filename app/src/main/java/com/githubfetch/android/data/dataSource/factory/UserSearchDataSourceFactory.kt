package com.githubfetch.android.data.dataSource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.githubfetch.android.api.GithubService
import com.githubfetch.android.data.dataSource.UserSearchDataSource
import com.githubfetch.android.data.model.User

class UserSearchDataSourceFactory constructor(
    val query: String,
    val service: GithubService
) : DataSource.Factory<Int, User>() {
    val userSearchLiveDataSource = MutableLiveData<UserSearchDataSource>()
    override fun create(): DataSource<Int, User> {
        val userSearchDataSource =
            UserSearchDataSource(
                query,
                service
            )
        userSearchLiveDataSource.postValue(userSearchDataSource)
        return userSearchDataSource
    }
}