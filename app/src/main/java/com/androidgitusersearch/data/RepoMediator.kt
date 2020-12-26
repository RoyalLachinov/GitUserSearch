package com.androidgitusersearch.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.androidgitusersearch.api.RepoApiService
import com.androidgitusersearch.model.RepoModel
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


@OptIn(ExperimentalPagingApi::class)
class RepoMediator(
    private val repoApiService: RepoApiService,
    private val userId:String):
    PagingSource<Int,RepoModel>() {

    companion object{
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoModel> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {

            val repoList = repoApiService.searchRepos(userId,position,params.loadSize)
            LoadResult.Page(
                data = repoList,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repoList.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}