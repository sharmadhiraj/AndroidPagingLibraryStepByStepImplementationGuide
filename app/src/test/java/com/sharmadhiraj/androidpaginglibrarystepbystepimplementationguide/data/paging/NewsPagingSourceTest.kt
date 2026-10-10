package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.paging

import androidx.paging.PagingSource
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote.ArticleDto
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote.NewsApi
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote.NewsResponse
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.domain.News
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NewsPagingSourceTest {

    private class FakeApi(private val handler: (Int, Int) -> NewsResponse) : NewsApi {
        val requestedPages = mutableListOf<Int>()

        override suspend fun getNews(page: Int, pageSize: Int): NewsResponse {
            requestedPages += page
            return handler(page, pageSize)
        }
    }

    private fun articles(count: Int) = NewsResponse(
        List(count) { ArticleDto(title = "T$it", url = "u$it", imageUrl = null) }
    )

    private fun refresh(key: Int?, size: Int = 2) =
        PagingSource.LoadParams.Refresh(key, size, false)

    @Test
    fun firstLoadRequestsPageOneAndReturnsNextKey() = runTest {
        val api = FakeApi { _, size -> articles(size) }

        val result = NewsPagingSource(api).load(refresh(null)) as PagingSource.LoadResult.Page

        assertEquals(listOf(1), api.requestedPages)
        assertEquals(2, result.data.size)
        assertEquals(2, result.nextKey)
        assertNull(result.prevKey)
    }

    @Test
    fun usesGivenKeyAsPage() = runTest {
        val api = FakeApi { _, size -> articles(size) }

        val result = NewsPagingSource(api).load(refresh(3)) as PagingSource.LoadResult.Page

        assertEquals(listOf(3), api.requestedPages)
        assertEquals(4, result.nextKey)
    }

    @Test
    fun emptyPageEndsPagination() = runTest {
        val api = FakeApi { _, _ -> articles(0) }

        val result = NewsPagingSource(api).load(refresh(null)) as PagingSource.LoadResult.Page

        assertNull(result.nextKey)
    }

    @Test
    fun shortPageDoesNotEndPagination() = runTest {
        val api = FakeApi { _, _ -> articles(1) }

        val result = NewsPagingSource(api).load(refresh(null)) as PagingSource.LoadResult.Page

        assertEquals(2, result.nextKey)
    }

    @Test
    fun removedArticlesDoNotEndPagination() = runTest {
        val api = FakeApi { _, _ ->
            NewsResponse(listOf(ArticleDto("[Removed]", "u1", null), ArticleDto("Ok", "u2", null)))
        }

        val result = NewsPagingSource(api).load(refresh(null)) as PagingSource.LoadResult.Page

        assertEquals(listOf(News("u2", "Ok", null)), result.data)
        assertEquals(2, result.nextKey)
    }

    @Test
    fun failureBecomesError() = runTest {
        val error = IOException("offline")
        val api = FakeApi { _, _ -> throw error }

        val result = NewsPagingSource(api).load(refresh(null))

        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(error, (result as PagingSource.LoadResult.Error).throwable)
    }
}
