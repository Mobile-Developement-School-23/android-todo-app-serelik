package com.serelik.todoapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serelik.todoapp.databinding.ItemNewTodoBinding
import com.serelik.todoapp.databinding.ItemTodoBinding
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoUiBaseItem

class TodoItemAdapter(
    private val onTodoClickListener: (id: String) -> Unit,
    private val changeIsDoneListener: (item: TodoItem, isDone: Boolean) -> Unit,
    private val onNewTodoClickListener: () -> Unit
) : ListAdapter<TodoUiBaseItem, RecyclerView.ViewHolder>(TodoItemCallback()) {

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
        if (holder is TodoItemViewHolder) {
            holder.bind(getItem(position) as TodoItem)
        }
    }

    fun getItemTodo(pos: Int): TodoItem? {
        return getItem(pos) as? TodoItem
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TodoItem -> TODO_ITEM
            else -> NEW_TODO_ITEM
        }
    }

    companion object {
        const val TODO_ITEM = 0
        const val NEW_TODO_ITEM = 1
    }
}
