package com.githubfetch.android.data.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.githubfetch.android.api.GithubService
import com.githubfetch.android.api.NetworkState
import com.githubfetch.android.api.enum.Status
import com.githubfetch.android.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDataSource(val service: GithubService) : PageKeyedDataSource<Int, User>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        networkState.postValue(NetworkState(Status.RUNNING))
        service.fetchUsersForPaging().enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                networkState.postValue(NetworkState(Status.FAILED, t.message))
            }

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                response.body()?.let {
                    callback.onResult(it, null, it.last().id)
                    networkState.postValue(NetworkState(Status.SUCCESS))
                } ?: run {
                    networkState.postValue(NetworkState(Status.FAILED))
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        networkState.postValue(NetworkState(Status.RUNNING))
        service.fetchUsersForPaging(params.key).enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                networkState.postValue(NetworkState(Status.FAILED, t.message))
            }

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                response.body()?.let {
                    callback.onResult(it, it.last().id)
                    networkState.postValue(NetworkState(Status.SUCCESS))
                } ?: run {
                    networkState.postValue(NetworkState(Status.FAILED))
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {}
}