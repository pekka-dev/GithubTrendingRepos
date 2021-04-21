package com.example.githubtrendingrepos.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.githubtrendingrepos.data.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val githubRepository: GithubRepository) :
	ViewModel() {
	private val currentQuery = MutableLiveData(DEFAULT_QUERY)

	val repos = currentQuery.switchMap { queryString ->
		githubRepository.getRepos(queryString).cachedIn(viewModelScope)
	}

	fun searchQuery(query: String) {
		currentQuery.value = query
	}

	companion object {
		const val DEFAULT_QUERY = "created:>2021-04-17"
	}
}