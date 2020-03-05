package com.githubfetch.android.view.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.githubfetch.android.data.repository.UserRepository
import com.githubfetch.android.api.NetworkState
import com.githubfetch.android.api.Result
import com.githubfetch.android.api.GithubService
import com.githubfetch.android.data.model.User

class MainViewModel constructor(application: Application) : AndroidViewModel(application) {
    private var userRepository: UserRepository =
        UserRepository(GithubService.create())
    private var _pagingUsers: LiveData<PagedList<User>> = MutableLiveData()
    val pagingUsers: LiveData<PagedList<User>>
        get() = _pagingUsers

    private var _state: LiveData<NetworkState> = MutableLiveData()
    val state: LiveData<NetworkState>
        get() = _state

    private val searchQuery = MutableLiveData<String>()
    private val searchResult: LiveData<Result<User>> = Transformations.map(searchQuery) {
        if (it.isEmpty())
            userRepository.getUsers()
        else
            userRepository.searchUser(it)
    }

    init {
        _pagingUsers = Transformations.switchMap(searchResult) {
            it.pagedList
        }

        _state = Transformations.switchMap(searchResult) {
            it.networkState
        }
    }

    fun searchUser(trim: String) {
        searchQuery.postValue(trim)
    }
}