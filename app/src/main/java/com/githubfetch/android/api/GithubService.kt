package com.githubfetch.android.api

import com.githubfetch.android.api.response.SearchResponse
import com.githubfetch.android.data.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("users")
    fun fetchUsersForPaging(@Query("since") lastId: Int? = null): Call<List<User>>

    @GET("search/users")
    fun searchUsersForPaging(@Query("q") query: String = "", @Query("page") page: Int? = null): Call<SearchResponse>

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GithubService {
            val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService::class.java)
        }
    }
}