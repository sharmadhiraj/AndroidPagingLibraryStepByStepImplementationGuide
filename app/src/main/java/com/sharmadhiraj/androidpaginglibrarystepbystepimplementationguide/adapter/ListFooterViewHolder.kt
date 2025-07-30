package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State.ERROR
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.State.LOADING
import com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.databinding.ItemListFooterBinding

class ListFooterViewHolder(
    private val binding: ItemListFooterBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(status: State?) {
        binding.progressBar.visibility = if (status == LOADING) View.VISIBLE else View.INVISIBLE
        binding.txtError.visibility = if (status == ERROR) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemListFooterBinding.inflate(inflater, parent, false)
            binding.txtError.setOnClickListener { retry() }
            return ListFooterViewHolder(binding)
        }
    }
}
