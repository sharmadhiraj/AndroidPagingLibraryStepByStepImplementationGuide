package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.adapter.NewsListAdapter
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State.ERROR
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State.LOADING
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.databinding.ActivityNewsListBinding
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.viewModel.NewsListViewModel

class NewsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsListBinding
    private lateinit var viewModel: NewsListViewModel
    private lateinit var newsListAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NewsListViewModel::class.java]

        initAdapter()
        initState()
    }

    private fun initAdapter() {
        newsListAdapter = NewsListAdapter { viewModel.retry() }
        binding.recyclerView.adapter = newsListAdapter

        viewModel.newsList.observe(this) {
            newsListAdapter.submitList(it)
        }
    }

    private fun initState() {
        binding.txtError.setOnClickListener { viewModel.retry() }

        viewModel.getState().observe(this) { state ->
            binding.progressBar.visibility =
                if (viewModel.listIsEmpty() && state == LOADING) VISIBLE else GONE
            binding.txtError.visibility =
                if (viewModel.listIsEmpty() && state == ERROR) VISIBLE else GONE

            if (!viewModel.listIsEmpty()) {
                newsListAdapter.setState(state ?: State.DONE)
            }
        }
    }
}
