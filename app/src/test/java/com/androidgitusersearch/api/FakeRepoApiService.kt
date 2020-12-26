package com.androidgitusersearch.api

import com.androidgitusersearch.model.RepoModel
import com.androidgitusersearch.model.UserModel

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

class FakeRepoApiService(var response: Any): RepoApiService {

    override suspend fun searchRepos(
        userId: String,
        page: Int,
        itemsPerPage: Int
    ): MutableList<RepoModel> {
        return (response as MutableList<*>).filterIsInstance<RepoModel>() as MutableList<RepoModel>
    }

    override suspend fun getUserById(userId: String) =
        response as UserModel
}