package com.githubfetch.android.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.githubfetch.android.api.GithubService
import com.githubfetch.android.api.NetworkState
import com.githubfetch.android.api.SearchResponse
import com.githubfetch.android.api.enum.Status
import com.githubfetch.android.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class UserSearchDataSource(
    val query: String,
    val service: GithubService
) : PageKeyedDataSource<Int, User>() {
    val networkState = MutableLiveData<NetworkState>()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        networkState.postValue(NetworkState(Status.RUNNING))
        service.searchUsersForPaging(query).enqueue(object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                networkState.postValue(NetworkState(Status.FAILED, t.message))
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                response.body()?.let {
                    callback.onResult(it.users, null, 2)
                    if (it.users.isNotEmpty())
                        networkState.postValue(NetworkState(Status.SUCCESS))
                    else
                        networkState.postValue(NetworkState(Status.EMPTY))
                } ?: run {
                    networkState.postValue(NetworkState(Status.FAILED))
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        networkState.postValue(NetworkState(Status.RUNNING))
        service.searchUsersForPaging(
            query,
            params.key + 1
        ).enqueue(object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                networkState.postValue(NetworkState(Status.FAILED, t.message))
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                response.body()?.let {
                    callback.onResult(it.users, params.key + 1)
                    networkState.postValue(NetworkState(Status.SUCCESS))
                } ?: run {
                    networkState.postValue(NetworkState(Status.FAILED))
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {}
}