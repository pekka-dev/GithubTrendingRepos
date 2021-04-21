package com.example.githubtrendingrepos.data.model


import com.google.gson.annotations.SerializedName

data class GithubRepo(
	val id: Int,
	val name: String,
	@SerializedName(value = "full_name")
	val fullName: String,
	val private: Boolean,
	@SerializedName(value = "html_url")
	val htmlUrl: String,
	val description: String,
	@SerializedName(value = "stargazers_count")
	val stars: String,
	val language: String
)
