package com.androidgitusersearch.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.map
import com.androidgitusersearch.JsonConverterUnitTest
import com.androidgitusersearch.data.RepoRepository
import com.androidgitusersearch.data.RepoViewModel
import com.androidgitusersearch.model.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mockito

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
                    Log.d("userModel", user.name.toString())
                }else{
                    val error = it.errorBody
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

        GlobalScope.launch(Dispatchers.Main) {

            viewModel.searchRepo("royallachinov").collect { pagingData ->
                pagingData.map {
                    it.let {
                        Log.d("repoName",it.name.toString())
                        Assert.assertEquals(it.name,"android-room-with-a-view-kotlin")

                    }
                }
            }
        }

    }

}