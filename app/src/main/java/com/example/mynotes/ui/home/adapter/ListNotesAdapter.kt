package com.example.mynotes.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.data.remote.response.DataItem
import com.example.mynotes.databinding.ItemListNotesBinding
import com.example.mynotes.utils.Helper.formattingDate

class ListNotesAdapter : ListAdapter<DataItem, ListNotesAdapter.ListNoteViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNoteViewHolder {
        val binding = ItemListNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListNoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListNoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ListNoteViewHolder(private val binding: ItemListNotesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem) {
            with(binding) {
                tvTitle.text = item.title
                tvContent.text = item.content
                tvDateUpdated.text = item.updatedAt
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<DataItem> =
            object : DiffUtil.ItemCallback<DataItem>() {
                override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                    return oldItem == newItem
                }
            }
    }

}