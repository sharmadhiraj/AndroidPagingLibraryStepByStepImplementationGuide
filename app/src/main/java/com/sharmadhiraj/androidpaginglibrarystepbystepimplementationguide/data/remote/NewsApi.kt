package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything?q=sports&apiKey=aa67d8d98c8e4ad1b4f16dbd5f3be348")
    suspend fun getNews(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponse
}
