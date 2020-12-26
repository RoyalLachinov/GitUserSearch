package com.androidgitusersearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.androidgitusersearch.R
import com.androidgitusersearch.api.ApiResponse
import com.androidgitusersearch.data.RepoViewModel
import com.androidgitusersearch.databinding.ActivityMainBinding
import com.androidgitusersearch.injection.ObjectInjector
import com.androidgitusersearch.model.UserModel
import com.androidgitusersearch.util.CustomSnackbar
import com.androidgitusersearch.util.HelperClass
import com.androidgitusersearch.util.HelperClass.hide
import com.androidgitusersearch.util.HelperClass.hideKeyboard
import com.androidgitusersearch.util.HelperClass.show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val viewBindingMainActivity get() = binding!!

    private lateinit var repoViewModel: RepoViewModel
    val repoAdapter = RepoAdapter()

    private var userSearchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBindingMainActivity.root)

        repoViewModel = ViewModelProvider(this, ObjectInjector.provideViewModelFactory())
            .get(RepoViewModel::class.java)

        binding!!.recyclerViewRepos.adapter = repoAdapter
        repoViewModel.getUserById("royallachinov")
        repoViewModel.userLiveData.observe(this@MainActivity, userObjectObserver)


        search(viewBindingMainActivity.editTextSearch.text.toString())
        initSearch(viewBindingMainActivity.editTextSearch.text.toString())

        viewBindingMainActivity.buttonSearch.setOnClickListener {
            viewBindingMainActivity.buttonSearch.hideKeyboard()
            updateUserRepoList()
        }


        repoAdapter.setItemClickListener(object : RepoAdapter.ItemClickListener{
            override fun onItemClicked(updateDate: String, starCount: Int, forksCount: Int) {
                CustomSnackbar.make(window.decorView.rootView as ViewGroup,
                    HelperClass. convertUpdateDate(updateDate), starCount, forksCount).show()
            }
        })

        repoAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            viewBindingMainActivity.recyclerViewRepos.isVisible = loadState.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            viewBindingMainActivity.pbRepos.isVisible = loadState.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            viewBindingMainActivity.textViewEmptyMessage.isVisible = loadState.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                viewBindingMainActivity.imageViewUserImage.hide()
                viewBindingMainActivity.textViewUserName.hide()
                viewBindingMainActivity.recyclerViewRepos.hide()
                viewBindingMainActivity.textViewEmptyMessage.show()
                viewBindingMainActivity.textViewEmptyMessage.text = it.error.message
            }
        }
    }

    private val userObjectObserver = Observer<ApiResponse> { apiResponse ->
        if (apiResponse.successBody != null) {
            val userModel = apiResponse.successBody as UserModel
            userModel.let {
                Glide.with(this@MainActivity)
                    .load(it.avatarUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(viewBindingMainActivity.imageViewUserImage)
                viewBindingMainActivity.textViewUserName.text = it.name
            }
        } else {
            val errorMessage = apiResponse.errorBody as String
            viewBindingMainActivity.imageViewUserImage.hide()
            viewBindingMainActivity.textViewUserName.hide()
            viewBindingMainActivity.recyclerViewRepos.hide()
            viewBindingMainActivity.textViewEmptyMessage.show()
            viewBindingMainActivity.textViewEmptyMessage.text = errorMessage
        }


    }

    private fun search(userId: String) {
        // Make sure we cancel the previous job before creating a new one
        userSearchJob?.cancel()
        userSearchJob = lifecycleScope.launch {
            repoViewModel.searchRepo(userId).collectLatest {
                repoAdapter.submitData(it)
            }
        }
    }

    private fun initSearch(query: String) {
        viewBindingMainActivity.editTextSearch.setText(query)

        viewBindingMainActivity.editTextSearch.addTextChangedListener {
            if (viewBindingMainActivity.editTextSearch.selectionStart == 0 || it.toString().trim().isNotEmpty()){
                viewBindingMainActivity.searchInputLayout.error = null
            } else {
                viewBindingMainActivity.searchInputLayout.error = getString(R.string.user_input_warning)
            }

        }

        viewBindingMainActivity.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateUserRepoList()
                true
            } else {
                false
            }
        }
        viewBindingMainActivity.editTextSearch.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateUserRepoList()
                true
            } else {
                false
            }
        }

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            repoAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { viewBindingMainActivity.recyclerViewRepos.scrollToPosition(0) }
        }
    }

    private fun updateUserRepoList() {
        viewBindingMainActivity.editTextSearch.hideKeyboard()

        if (viewBindingMainActivity.editTextSearch.text?.toString()!!.trim().isNotEmpty()) {
            viewBindingMainActivity.searchInputLayout.error = null
            search(viewBindingMainActivity.editTextSearch.text.toString())
        } else {
            viewBindingMainActivity.searchInputLayout.error = getString(R.string.user_input_warning)
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}