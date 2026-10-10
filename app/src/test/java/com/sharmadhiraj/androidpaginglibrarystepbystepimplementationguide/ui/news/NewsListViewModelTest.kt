package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.ui.news

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.NewsRepository
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.domain.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {

    private val items = listOf(News("1", "One", null), News("2", "Two", "img"))

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun exposesRepositoryNews() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repository = object : NewsRepository {
            override fun getNews(): Flow<PagingData<News>> = Pager(PagingConfig(pageSize = 10)) { ItemsPagingSource(items) }.flow
        }

        val viewModel = NewsListViewModel(repository)

        assertEquals(items, viewModel.news.asSnapshot())
    }

    private class ItemsPagingSource(private val items: List<News>) : PagingSource<Int, News>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> =
            LoadResult.Page(items, prevKey = null, nextKey = null)

        override fun getRefreshKey(state: PagingState<Int, News>): Int? = null
    }
}
