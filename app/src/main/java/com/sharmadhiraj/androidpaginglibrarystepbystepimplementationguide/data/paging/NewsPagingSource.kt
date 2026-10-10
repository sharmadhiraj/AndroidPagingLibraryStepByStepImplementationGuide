package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote.NewsApi
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote.toNewsList
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.domain.News
import kotlin.coroutines.cancellation.CancellationException

// NewsAPI can return fewer items than requested on non-final pages, so only an empty page ends paging.
class NewsPagingSource(private val api: NewsApi) : PagingSource<Int, News>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val response = api.getNews(page, params.loadSize)
            val isLastPage = response.articles.isNullOrEmpty()
            LoadResult.Page(
                data = response.toNewsList(),
                prevKey = null,
                nextKey = if (isLastPage) null else page + 1
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, News>): Int? = null

    companion object {
        const val FIRST_PAGE = 1
    }
}
