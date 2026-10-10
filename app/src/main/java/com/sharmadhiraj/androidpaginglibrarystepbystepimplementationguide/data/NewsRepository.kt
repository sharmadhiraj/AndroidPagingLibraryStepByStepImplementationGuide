package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.paging.NewsPagingSource
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote.NewsApi
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.domain.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<PagingData<News>>
}

class NetworkNewsRepository(private val api: NewsApi) : NewsRepository {

    override fun getNews(): Flow<PagingData<News>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NewsPagingSource(api) }
    ).flow

    companion object {
        const val PAGE_SIZE = 10
    }
}
