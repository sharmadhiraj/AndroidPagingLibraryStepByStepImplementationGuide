package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.NetworkService
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.News
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.NewsDataSource
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.NewsDataSourceFactory
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers

class NewsListViewModel : ViewModel() {

    private val networkService = NetworkService.getService()
    var newsList: LiveData<PagedList<News>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val newsDataSourceFactory: NewsDataSourceFactory =
        NewsDataSourceFactory(compositeDisposable, networkService)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        newsList = Pager<Int, News>(
            PagingConfig(
                config.pageSize,
                config.prefetchDistance,
                config.enablePlaceholders,
                config.initialLoadSizeHint,
                config.maxSize
            ),
            this.initialLoadKey,
            newsDataSourceFactory.asPagingSourceFactory(Dispatchers.IO)
        ).liveData.build()
    }


    fun getState(): LiveData<State> = Transformations.switchMap(
        newsDataSourceFactory.newsDataSourceLiveData,
        NewsDataSource::state
    )

    fun retry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return newsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}