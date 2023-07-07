package com.serelik.todoapp.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.serelik.todoapp.model.NewTodo
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoUiBaseItem

class TodoItemCallback : DiffUtil.ItemCallback<TodoUiBaseItem>() {
    override fun areItemsTheSame(oldItem: TodoUiBaseItem, newItem: TodoUiBaseItem): Boolean {
        if (oldItem is NewTodo && newItem is NewTodo) return true
        if (oldItem !is TodoItem || newItem !is TodoItem) return false

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoUiBaseItem, newItem: TodoUiBaseItem): Boolean {
        if (oldItem is NewTodo && newItem is NewTodo) return true
        if (oldItem !is TodoItem || newItem !is TodoItem) return false

        return (oldItem as TodoItem) == (newItem as TodoItem)
    }
}
