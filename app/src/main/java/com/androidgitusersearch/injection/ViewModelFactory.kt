package com.androidgitusersearch.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidgitusersearch.data.RepoRepository
import com.androidgitusersearch.data.RepoViewModel

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


class ViewModelFactory (private val repository: RepoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}