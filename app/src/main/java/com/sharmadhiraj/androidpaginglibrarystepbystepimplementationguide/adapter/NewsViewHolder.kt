package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.R
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.img_news_banner
import kotlinx.android.synthetic.main.item_news.view.txt_news_name

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(news: News?) {
        if (news != null) {
            itemView.txt_news_name.text = news.title
            if (!news.image.isNullOrEmpty())
                Picasso.get().load(news.image).into(itemView.img_news_banner)
        }
    }

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)
            return NewsViewHolder(view)
        }
    }
}