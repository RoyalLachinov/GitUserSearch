package com.androidgitusersearch.api

import com.androidgitusersearch.model.RepoModel
import com.androidgitusersearch.model.UserModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


interface RepoApiService {

    @GET("users/{userId}/repos")
    suspend fun searchRepos(
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): MutableList<RepoModel>


    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): UserModel

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun createRetrofit(): RepoApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RepoApiService::class.java)
        }
    }
}