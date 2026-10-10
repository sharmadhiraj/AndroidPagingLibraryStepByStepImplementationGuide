package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.di

import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.NetworkNewsRepository
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.NewsRepository
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote.NewsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {

    private val newsApi: NewsApi = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)

    val newsRepository: NewsRepository = NetworkNewsRepository(newsApi)
}
