package com.githubfetch.android.api

import com.githubfetch.android.api.enum.Status

data class NetworkState(val status: Status, val msg: String? = null)