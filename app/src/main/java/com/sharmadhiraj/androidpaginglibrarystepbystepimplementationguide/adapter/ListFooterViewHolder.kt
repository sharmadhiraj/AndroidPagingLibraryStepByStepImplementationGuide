package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.R
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State.ERROR
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State.LOADING
import kotlinx.android.synthetic.main.item_list_footer.view.*

class ListFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: State?) {
        itemView.progress_bar.visibility = if (status == LOADING) VISIBLE else INVISIBLE
        itemView.txt_error.visibility = if (status == ERROR) VISIBLE else INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_footer, parent, false)
            view.txt_error.setOnClickListener { retry() }
            return ListFooterViewHolder(view)
        }
    }
}