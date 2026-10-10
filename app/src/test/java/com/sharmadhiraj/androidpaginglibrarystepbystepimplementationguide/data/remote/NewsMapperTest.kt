package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote

import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.domain.News
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NewsMapperTest {

    @Test
    fun mapsValidArticle() {
        val article = ArticleDto(title = "Title", url = "https://a.com/1", imageUrl = "https://a.com/1.png")

        assertEquals(News("https://a.com/1", "Title", "https://a.com/1.png"), article.toNewsOrNull())
    }

    @Test
    fun blankImageBecomesNull() {
        val article = ArticleDto(title = "Title", url = "https://a.com/1", imageUrl = " ")

        assertNull(article.toNewsOrNull()?.imageUrl)
    }

    @Test
    fun dropsArticlesWithoutTitleOrUrlOrRemoved() {
        assertNull(ArticleDto(title = null, url = "u", imageUrl = null).toNewsOrNull())
        assertNull(ArticleDto(title = "T", url = null, imageUrl = null).toNewsOrNull())
        assertNull(ArticleDto(title = "[Removed]", url = "u", imageUrl = null).toNewsOrNull())
    }

    @Test
    fun nullArticlesGivesEmptyList() {
        assertEquals(emptyList<News>(), NewsResponse(articles = null).toNewsList())
    }
}
