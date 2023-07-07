package com.serelik.todoapp.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serelik.todoapp.databinding.ItemNewTodoBinding
import com.serelik.todoapp.databinding.ItemTodoBinding
import com.serelik.todoapp.model.TodoItem

class TodoItemAdapter(
    private val onTodoClickListener: (id: String) -> Unit,
    private val changeIsDoneListener: (item: TodoItem, isDone: Boolean) -> Unit,
    private val onNewTodoClickListener: () -> Unit,
) : ListAdapter<TodoItem, RecyclerView.ViewHolder>(TodoItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TODO_ITEM) {
            val binding = ItemTodoBinding.inflate(inflater, parent, false)
            TodoItemViewHolder(
                binding,
                onTodoClickListener,
                changeIsDoneListener
            )
        } else {
            val binding = ItemNewTodoBinding.inflate(inflater, parent, false)
            NewTodoItemViewHolder(binding, onNewTodoClickListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoItemViewHolder)
            holder.bind(getItem(position))
    }

    fun getItemTodo(pos: Int): TodoItem {
        return getItem(pos)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount == position + 1)
            NEW_TODO_ITEM
        else TODO_ITEM
    }

    companion object {
        const val TODO_ITEM = 0
        const val NEW_TODO_ITEM = 1
    }
}