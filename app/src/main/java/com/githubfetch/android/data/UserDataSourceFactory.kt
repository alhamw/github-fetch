package com.githubfetch.android.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.githubfetch.android.api.GithubService
import com.githubfetch.android.data.model.User

class UserDataSourceFactory(val service: GithubService) : DataSource.Factory<Int, User>() {
    val userLiveDataSource = MutableLiveData<UserDataSource>()
    override fun create(): DataSource<Int, User> {
        val userDataSource = UserDataSource(service)
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }
}