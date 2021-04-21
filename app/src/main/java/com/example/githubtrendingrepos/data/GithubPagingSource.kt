package com.example.githubtrendingrepos.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubtrendingrepos.api.GithubApi
import com.example.githubtrendingrepos.data.model.GithubRepo
import retrofit2.HttpException
import java.io.IOException


private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubPagingSource(private val githubApi: GithubApi, private val query: String) :
    PagingSource<Int, GithubRepo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepo> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX

        return try {
            val response = githubApi.getRepos("stars", "desc", query, params.loadSize, position)
            val repos = response.items
            Log.d("Github_API_res", repos.size.toString())
            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            Log.e("Github_API_error", e.message.toString())
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("Github_API_error", e.message.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubRepo>): Int? {
        TODO("Not yet implemented")
    }
}