package com.androidgitusersearch.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidgitusersearch.databinding.ItemRepoBinding
import com.androidgitusersearch.model.RepoModel

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


class RepoAdapter: PagingDataAdapter<RepoModel, RecyclerView.ViewHolder>(pagingDataAdapterDiffCallback) {

    lateinit var clickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.clickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClicked(updateDate:String, starCount:Int,forksCount:Int)
    }

    companion object {
        val pagingDataAdapterDiffCallback = object : DiffUtil.ItemCallback<RepoModel>() {
            override fun areItemsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
                return oldItem.updateDate == newItem.updateDate
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position) as RepoModel
        val binding = (holder as ViewHolder).binding
        repoItem.let {
            binding.textViewRepoName.text = repoItem.name
            binding.textViewRepoDescription.text = repoItem.description
        }

        binding.rootView.setOnClickListener {
            clickListener.onItemClicked(repoItem.updateDate!!,repoItem.starCount,repoItem.forks)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemRepoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    class ViewHolder(val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root)
}