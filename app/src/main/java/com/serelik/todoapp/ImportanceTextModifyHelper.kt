package com.serelik.todoapp

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import com.serelik.todoapp.model.TodoItemImportance

object ImportanceTextModifyHelper {
    fun modifyText(
        text: String,
        importance: TodoItemImportance,
        context: Context
    ): SpannableString {
        val spannableText = SpannableString("  $text ")
        val drawableRes = if (importance == TodoItemImportance.HIGH) {
            R.drawable.ic_priority_high
        } else {
            R.drawable.ic_priority_low
        }

        spannableText.setSpan(
            ImageSpan(context, drawableRes),
            0,
            1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableText
    }
}
