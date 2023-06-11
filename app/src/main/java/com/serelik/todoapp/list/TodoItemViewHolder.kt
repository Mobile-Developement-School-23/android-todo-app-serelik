package com.serelik.todoapp.list

import androidx.recyclerview.widget.RecyclerView
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.ItemTodoBinding
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import java.time.format.DateTimeFormatter

class TodoItemViewHolder(
    private val binding: ItemTodoBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoItem) {
        binding.textViewTodo.text = item.text
        binding.textViewDate.text = item.created.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
        binding.checkbox.isChecked = item.isDone

        val buttonDrawableRes =
            if (item.importance == TodoItemImportance.HIGH) R.drawable.selector_checkbox_high else R.drawable.selector_checkbox

        binding.checkbox.setButtonIconDrawableResource(buttonDrawableRes)

    }

}
