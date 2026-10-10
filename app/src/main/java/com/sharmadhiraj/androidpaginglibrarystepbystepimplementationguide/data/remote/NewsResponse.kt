package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val articles: List<ArticleDto>?
)

data class ArticleDto(
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("urlToImage")
    val imageUrl: String?
)
