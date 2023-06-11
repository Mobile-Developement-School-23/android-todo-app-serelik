package com.serelik.todoapp.list

import androidx.recyclerview.widget.DiffUtil
import com.serelik.todoapp.model.TodoItem

class TodoItemCallback : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem
}