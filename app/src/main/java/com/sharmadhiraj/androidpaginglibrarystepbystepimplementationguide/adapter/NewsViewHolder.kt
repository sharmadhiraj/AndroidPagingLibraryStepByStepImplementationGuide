package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.News
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News?) {
        if (news != null) {
            binding.txtNewsName.text = news.title
            if (!news.image.isNullOrEmpty()) {
                Picasso.get().load(news.image).into(binding.imgNewsBanner)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {
            val binding = ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return NewsViewHolder(binding)
        }
    }
}