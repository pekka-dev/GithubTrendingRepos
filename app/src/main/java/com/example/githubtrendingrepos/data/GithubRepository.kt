package com.example.githubtrendingrepos.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.githubtrendingrepos.api.GithubApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val githubApi: GithubApi) {

    fun getRepos(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                maxSize = 1000,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(this.githubApi, query) }
        ).liveData
}