package com.serelik.todoapp.list

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StrikethroughSpan
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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

        binding.textViewTodo.text =
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

                item.importance != TodoItemImportance.NONE -> modifyText(item)

                else -> item.text
            }

        if (item.deadline != null) {
            binding.textViewDate.isVisible = true
            binding.textViewDate.text =
                item.deadline.format(dateFormatter)
        } else binding.textViewDate.isVisible = false
        binding.checkbox.isChecked = item.isDone

        val buttonDrawableRes =
            if (item.importance == TodoItemImportance.HIGH) R.drawable.selector_checkbox_high else R.drawable.selector_checkbox

        binding.checkbox.setButtonIconDrawableResource(buttonDrawableRes)

    }

    private fun modifyText(item: TodoItem): SpannableString {
        val spannableText = SpannableString("  ${item.text} ")
        val drawableRes = if (item.importance == TodoItemImportance.HIGH)
            R.drawable.ic_priority_high
        else R.drawable.ic_priority_low

        spannableText.setSpan(
            ImageSpan(itemView.context, drawableRes),
            0,
            1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableText
    }

    companion object {
        private val dateFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern("d MMMM yyyy") }
    }

}
