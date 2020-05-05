package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.R
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.adapter.NewsListAdapter
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State.ERROR
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State.LOADING
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.viewModel.NewsListViewModel
import kotlinx.android.synthetic.main.activity_news_list.*

class NewsListActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsListViewModel
    private lateinit var newsListAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        viewModel = ViewModelProvider(this).get(NewsListViewModel::class.java)
        initAdapter()
        initState()
    }

    private fun initAdapter() {
        newsListAdapter = NewsListAdapter { viewModel.retry() }
        recycler_view.adapter = newsListAdapter
        viewModel.newsList.observe(this,
                Observer {
                    newsListAdapter.submitList(it)
                })
    }

    private fun initState() {
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility = if (viewModel.listIsEmpty() && state == LOADING) VISIBLE else GONE
            txt_error.visibility = if (viewModel.listIsEmpty() && state == ERROR) VISIBLE else GONE
            if (!viewModel.listIsEmpty()) {
                newsListAdapter.setState(state ?: State.DONE)
            }
        })
    }

}