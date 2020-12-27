package com.androidgitusersearch.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.androidgitusersearch.JsonConverterUnitTest
import com.androidgitusersearch.data.RepoRepository
import com.androidgitusersearch.data.RepoViewModel
import com.androidgitusersearch.model.RepoModel
import com.androidgitusersearch.model.UserModel
import com.androidgitusersearch.repo.FakeRepoMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mockito
import java.lang.reflect.Type

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

@ExperimentalCoroutinesApi
class RepoViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //Mock
     private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
     private val repoRepository = mock<RepoRepository>()
     private val viewModel = RepoViewModel(repoRepository)

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getUser() {

        /**
         * In this function I get mock user live data and checked its observation
         */

        GlobalScope.launch(Dispatchers.Main) {
            val mockUser:UserModel =
                Gson().fromJson(
                    JsonConverterUnitTest.getJsonFile("user.json"),
                    UserModel::class.java
                )
           /* val repoRepository = RepoRepository(FakeRepoApiService(mockUser))
            val viewModel = RepoViewModel(repoRepository)*/

            Mockito.`when`(repoRepository.getUserById("royallachinov"))
                .thenReturn(mockUser)

            viewModel.getUserById("royallachinov")
            viewModel.userLiveData.observeForever {
                if(it.successBody != null){
                    val user = it.successBody as UserModel

                    Assert.assertNotNull(it.successBody != null)
                    Assert.assertNotNull(user.name)
                    Assert.assertEquals(mockUser.name, "Royal Lachinov")
                    Assert.assertEquals(user.name, "Royal Lachinov")

                    Assert.assertNotNull(mockUser.avatarUrl)
                    Assert.assertNotNull(user.avatarUrl)
                    Assert.assertEquals(
                        mockUser.avatarUrl,
                        "https://avatars1.githubusercontent.com/u/10083621?v=4"
                    )
                    Assert.assertEquals(
                        user.avatarUrl,
                        "https://avatars1.githubusercontent.com/u/10083621?v=4"
                    )

                    Log.d("userModel", user.name.toString())
                }else{
                    val error = it.errorBody
                    Assert.assertNull(error)
                    Log.d("userErrorMessage",error.toString())
                }
            }
        }

    }

    @Test
    fun getUserRepo() {

        /**
         * In this function I tried get mock repo list. As I user Paging 3 for repoList in the MainActivity,
         * below test case might not be correct, because could not find proper test example from android.developers.com
         * If I would use LiveData it'd be very easy to write test case
         */

        val type: Type = object : TypeToken<List<RepoModel?>?>() {}.type
        val repoList: MutableList<RepoModel> = Gson().fromJson(JsonConverterUnitTest
            .getJsonFile("repo_list.json"), type)


        GlobalScope.launch(Dispatchers.Main) {

            val  pagingDataFlow: Flow<PagingData<RepoModel>> = Pager(
                config = PagingConfig(pageSize = 1, enablePlaceholders = false),
                pagingSourceFactory = {
                    FakeRepoMediator(repoList)
                }
            ).flow

            Mockito.`when`(viewModel.searchRepo("royallachinov"))
                .thenReturn(pagingDataFlow)

/*            viewModel.searchRepo("royallachinov").collect { pagingData ->
                pagingData.map {
                    it.let {
                        Log.d("repoName",it.name.toString())
                        Assert.assertEquals(it.name,"android-room-with-a-view-kotlin")

                    }
                }
            }*/
        }

    }

}