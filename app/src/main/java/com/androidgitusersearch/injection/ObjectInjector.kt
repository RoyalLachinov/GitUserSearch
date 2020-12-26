package com.androidgitusersearch.injection

import androidx.lifecycle.ViewModelProvider
import com.androidgitusersearch.api.RepoApiService
import com.androidgitusersearch.data.RepoRepository

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


object ObjectInjector{

    private fun provideGithubRepository(): RepoRepository {
        return RepoRepository(RepoApiService.createRetrofit())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository())
    }
}