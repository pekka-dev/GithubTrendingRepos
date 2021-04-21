package com.example.githubtrendingrepos.di

import com.example.githubtrendingrepos.api.GithubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun provideRetrofit(): Retrofit =
		Retrofit.Builder()
			.baseUrl(GithubApi.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()

	@Provides
	@Singleton
	fun provideGithubApi(retrofit: Retrofit): GithubApi =
		retrofit.create(GithubApi::class.java)

}