package com.androidgitusersearch.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


data class RepoModel(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("updated_at")
    var updateDate: String? = null,
    @SerializedName("stargazers_count")
    var starCount: Int = 0,
    @SerializedName("forks")
    var forks: Int = 0
)
