package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("articles")
    val news: List<News>
)

data class News(
    val title: String,
    @SerializedName("urlToImage")
    val image: String?
)