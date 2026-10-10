package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.ui.news

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.NewsApplication
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.R
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.databinding.ActivityNewsListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class NewsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsListBinding
    private val newsAdapter = NewsAdapter()

    private val viewModel: NewsListViewModel by viewModels {
        NewsListViewModel.Factory((application as NewsApplication).container.newsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()

        binding.recyclerView.adapter =
            newsAdapter.withLoadStateFooter(NewsLoadStateAdapter { newsAdapter.retry() })
        binding.swipeRefresh.setOnRefreshListener { newsAdapter.refresh() }
        binding.retryButton.setOnClickListener { newsAdapter.retry() }

        observeNews()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun observeNews() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.news.collectLatest(newsAdapter::submitData) }
                launch {
                    newsAdapter.loadStateFlow
                        .distinctUntilChangedBy { it.refresh }
                        .collect(::render)
                }
            }
        }
    }

    private fun render(states: CombinedLoadStates) {
        val refresh = states.refresh
        val isEmpty = newsAdapter.itemCount == 0

        binding.swipeRefresh.isRefreshing = refresh is LoadState.Loading
        binding.recyclerView.isVisible = !isEmpty
        binding.stateGroup.isVisible = isEmpty && refresh !is LoadState.Loading

        when {
            isEmpty && refresh is LoadState.Error -> {
                binding.stateMessage.setText(R.string.error_message)
                binding.retryButton.isVisible = true
            }
            isEmpty && refresh is LoadState.NotLoading -> {
                binding.stateMessage.setText(R.string.empty_message)
                binding.retryButton.isVisible = false
            }
            !isEmpty && refresh is LoadState.Error ->
                Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry) { newsAdapter.retry() }
                    .show()
        }
    }
}
