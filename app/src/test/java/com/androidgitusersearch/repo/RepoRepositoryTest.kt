package com.androidgitusersearch.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import com.androidgitusersearch.CoroutineTestRule
import com.androidgitusersearch.JsonConverterUnitTest
import com.androidgitusersearch.api.FakeRepoApiService
import com.androidgitusersearch.data.RepoRepository
import com.androidgitusersearch.model.RepoModel
import com.androidgitusersearch.model.UserModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

@ExperimentalCoroutinesApi
class RepoRepositoryTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getRepoUser(){

        /**
         * In this function I get mock user object and check if user is valid
         */

        val mockUser = Gson().fromJson(
            JsonConverterUnitTest.getJsonFile("user.json"),
            UserModel::class.java
        )
        val repoRepository = RepoRepository(FakeRepoApiService(mockUser))

        GlobalScope.launch(Dispatchers.Main) {
            //Checking if api is successful
            repoRepository.getUserById("royallachinov")
            Assert.assertEquals(repoRepository.getUserById("royallachinov"), mockUser)
        }
    }

    @Test
    fun getUserRepoList(){

        /**
         * In this function I tried get mock repo list. As I user Paging 3 for repoList in the MainActivity,
         * below test case might not be correct, because could not find proper test example from android.developers.com
         * If I would use LiveData it'd be very easy to write test case
         */

        val type: Type = object : TypeToken<List<RepoModel?>?>() {}.type
        val repoList: MutableList<RepoModel> = Gson().fromJson(JsonConverterUnitTest
            .getJsonFile("repo_list.json"), type)

        val repoRepository = RepoRepository(FakeRepoApiService(repoList))


        GlobalScope.launch(Dispatchers.Main){

            val  pagingDataFlow: Flow<PagingData<RepoModel>> = Pager(
                config = PagingConfig(pageSize = 1, enablePlaceholders = false),
                pagingSourceFactory = {
                    FakeRepoMediator(repoList)
                }
            ).flow

            Assert.assertNotNull(pagingDataFlow)
            Assert.assertNotNull(repoRepository.getSearchResultStream("royallachinov"))
            //Assert.assertEquals(pagingDataFlow,mockResponse)
            //Assert.assertSame(pagingDataFlow,mockResponse)
        }
    }
}