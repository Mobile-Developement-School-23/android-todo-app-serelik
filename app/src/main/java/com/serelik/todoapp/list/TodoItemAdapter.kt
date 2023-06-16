package com.serelik.todoapp.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.serelik.todoapp.databinding.ItemTodoBinding
import com.serelik.todoapp.model.TodoItem


class TodoItemAdapter(
    private val onTodoClickListener: (id: String) -> Unit,
    private val changeIsDoneListener: (id: String, isDone: Boolean) -> Unit
) : ListAdapter<TodoItem, TodoItemViewHolder>(TodoItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTodoBinding.inflate(inflater, parent, false)
        return TodoItemViewHolder(
            binding,
            onTodoClickListener,
            changeIsDoneListener
        )
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemTodo(pos: Int): TodoItem {
        return getItem(pos)
    }
}