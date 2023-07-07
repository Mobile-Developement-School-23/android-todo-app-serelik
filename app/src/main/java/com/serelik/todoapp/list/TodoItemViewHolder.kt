package com.serelik.todoapp.list

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.serelik.todoapp.DateFormatterHelper
import com.serelik.todoapp.ImportanceTextModifyHelper
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.ItemTodoBinding
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance

class TodoItemViewHolder(
    private val binding: ItemTodoBinding,
    private val onTodoClickListener: (id: String) -> Unit,
    private val changeIsDoneListener: (item: TodoItem, isDone: Boolean) -> Unit

) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TodoItem) = with(binding) {
        root.setOnClickListener { onTodoClickListener(item.id) }

        checkbox.setOnCheckedChangeListener { checkBoxView, isChecked ->
            if (!checkBoxView.isPressed) {
                return@setOnCheckedChangeListener
            }
            changeIsDoneListener(item, isChecked)
        }

        formatText(item)
        setupDeadline(item)
        setupCheckBox(item)
    }

    private fun formatText(item: TodoItem) {
        binding.textViewTodo.text = when {
            item.isDone -> formatIsDoneText(item.text)

            item.importance != TodoItemImportance.NONE -> ImportanceTextModifyHelper.modifyText(
                item.text,
                item.importance,
                itemView.context
            )

            else -> item.text
        }
    }

    private fun formatIsDoneText(text: String): SpannableString {
        val strikeThroughText = SpannableString(text)
        strikeThroughText.setSpan(
            StrikethroughSpan(),
            0,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val foregroundColor = ContextCompat.getColor(
            itemView.context,
            R.color.label_tertiary
        )

        strikeThroughText.setSpan(
            ForegroundColorSpan(foregroundColor),
            0,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return strikeThroughText
    }

    private fun setupDeadline(item: TodoItem) = with(binding) {
        if (item.deadline != null) {
            textViewDate.isVisible = true
            textViewDate.text = DateFormatterHelper.format(item.deadline)
        } else {
            textViewDate.isVisible = false
        }
    }

    private fun setupCheckBox(item: TodoItem) = with(binding) {
        checkbox.isChecked = item.isDone
        val buttonDrawableRes =
            if (item.importance == TodoItemImportance.HIGH) {
                R.drawable.selector_checkbox_high
            } else {
                R.drawable.selector_checkbox
            }

        checkbox.setButtonIconDrawableResource(buttonDrawableRes)
    }
}
