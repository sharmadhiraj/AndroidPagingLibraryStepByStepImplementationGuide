package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.NewsRepository
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.domain.News
import kotlinx.coroutines.flow.Flow

class NewsListViewModel(repository: NewsRepository) : ViewModel() {

    val news: Flow<PagingData<News>> = repository.getNews().cachedIn(viewModelScope)

    class Factory(private val repository: NewsRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NewsListViewModel(repository) as T
    }
}
