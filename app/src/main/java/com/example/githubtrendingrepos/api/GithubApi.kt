package com.example.githubtrendingrepos.api

import com.example.githubtrendingrepos.api.model.GithubResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }

    @GET("search/repositories")
    suspend fun getRepos(
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("q") q: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): GithubResponse
}