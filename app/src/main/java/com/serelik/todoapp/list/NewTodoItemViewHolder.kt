package com.serelik.todoapp.list

import androidx.recyclerview.widget.RecyclerView
import com.serelik.todoapp.databinding.ItemNewTodoBinding

class NewTodoItemViewHolder(
    binding: ItemNewTodoBinding,
    private val onNewTodoClickListener: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onNewTodoClickListener()
        }
    }

}
