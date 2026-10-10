package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote

import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.domain.News

private const val REMOVED_TITLE = "[Removed]"

fun ArticleDto.toNewsOrNull(): News? {
    val id = url?.takeIf { it.isNotBlank() } ?: return null
    val title = title?.takeIf { it.isNotBlank() && it != REMOVED_TITLE } ?: return null
    return News(id = id, title = title, imageUrl = imageUrl?.takeIf { it.isNotBlank() })
}

fun NewsResponse.toNewsList(): List<News> = articles.orEmpty().mapNotNull { it.toNewsOrNull() }
