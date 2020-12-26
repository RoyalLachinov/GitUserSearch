package com.androidgitusersearch.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.androidgitusersearch.api.RepoApiService
import com.androidgitusersearch.model.RepoModel
import com.androidgitusersearch.model.UserModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


class RepoRepository (private val repoApiService: RepoApiService) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 1
    }

    fun getSearchResultStream(userId: String): Flow<PagingData<RepoModel>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                RepoMediator(repoApiService, userId)
            }
        ).flow
    }

    suspend fun getUserById(userId:String): UserModel =
        repoApiService.getUserById(userId)
}