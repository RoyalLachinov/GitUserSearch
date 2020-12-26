package com.androidgitusersearch.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


data class UserModel(
    @SerializedName("name")
    var name:String? = null,
    @SerializedName("avatar_url")
    var avatarUrl:String? = null
)
