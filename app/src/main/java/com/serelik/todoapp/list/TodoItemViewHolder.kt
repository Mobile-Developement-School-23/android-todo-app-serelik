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

        root.setOnClickListener {
            onTodoClickListener(item.id)
        }

        checkbox.setOnCheckedChangeListener { checkBoxView, isChecked ->
            if (!checkBoxView.isPressed)
                return@setOnCheckedChangeListener
            changeIsDoneListener(item, isChecked)

        }

        textViewTodo.text =
            when {
                item.isDone -> {
                    val strikeThroughText = SpannableString(item.text)
                    strikeThroughText.setSpan(
                        StrikethroughSpan(),
                        0,
                        item.text.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    strikeThroughText.setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.label_tertiary
                            )
                        ), 0, item.text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    strikeThroughText
                }

                item.importance != TodoItemImportance.NONE -> ImportanceTextModifyHelper.modifyText(
                    item.text, item.importance, itemView.context
                )

                else -> item.text
            }

        if (item.deadline != null) {
            textViewDate.isVisible = true
            textViewDate.text =
                DateFormatterHelper.format(item.deadline)
        } else textViewDate.isVisible = false
        checkbox.isChecked = item.isDone

        val buttonDrawableRes =
            if (item.importance == TodoItemImportance.HIGH)
                R.drawable.selector_checkbox_high
            else
                R.drawable.selector_checkbox

        checkbox.setButtonIconDrawableResource(buttonDrawableRes)
    }
}
