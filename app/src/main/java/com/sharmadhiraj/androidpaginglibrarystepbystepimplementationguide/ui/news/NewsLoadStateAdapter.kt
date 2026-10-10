package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.databinding.ItemLoadStateBinding

class NewsLoadStateAdapter(private val onRetry: () -> Unit) :
    LoadStateAdapter<NewsLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = LoadStateViewHolder(
        ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onRetry
    )

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class LoadStateViewHolder(
        private val binding: ItemLoadStateBinding,
        onRetry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { onRetry() }
        }

        fun bind(loadState: LoadState) {
            binding.progress.isVisible = loadState is LoadState.Loading
            binding.errorGroup.isVisible = loadState is LoadState.Error
        }
    }
}
