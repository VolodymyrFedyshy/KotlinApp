package com.app.lotlin.kotlinapp.data

object SearchRepositoryProvider {

    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(GithubApiService.Factory.create())
    }

}