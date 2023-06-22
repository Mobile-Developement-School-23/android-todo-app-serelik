package com.serelik.todoapp.edit

import android.content.Context
import android.text.SpannableString
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TodoEditSpannableAdapter(
    context: Context,
    textViewResourceId: Int,
    objects: Array<SpannableString>
) : ArrayAdapter<SpannableString>(context, textViewResourceId, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view as? TextView ?: return view

        textView.text = getItem(position)
        return textView
    }
}