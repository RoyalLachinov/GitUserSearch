package com.androidgitusersearch.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.androidgitusersearch.api.ApiResponse
import com.androidgitusersearch.model.RepoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


class RepoViewModel(private val repository: RepoRepository): ViewModel() {

    private var currentUserId: String? = null
    private var currentResult: Flow<PagingData<RepoModel>>? = null

    fun searchRepo(userId: String): Flow<PagingData<RepoModel>> {
        val lastResult = currentResult
        if (userId == currentUserId && lastResult != null) {
            return lastResult
        }
        currentUserId = userId
        val newResult: Flow<PagingData<RepoModel>> = repository.getSearchResultStream(userId)
            //.cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

    private var _userLiveData: MutableLiveData<ApiResponse> = MutableLiveData()
    val userLiveData: LiveData<ApiResponse> get() = _userLiveData

     fun getUserById(userId: String){
         viewModelScope.launch {
             try {
                 _userLiveData.value = ApiResponse(repository.getUserById(userId))
             }catch (exception:Exception){
                 _userLiveData.value = ApiResponse(exception.message!!)
             }
         }
     }

}