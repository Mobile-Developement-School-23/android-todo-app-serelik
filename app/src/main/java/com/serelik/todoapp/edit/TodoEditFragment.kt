package com.serelik.todoapp.edit

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.ImportanceTextModifyHelper
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.FragmentTodoEditBinding
import com.serelik.todoapp.model.TodoItemImportance

class TodoEditFragment : Fragment() {
    private val viewBinding by viewBinding(FragmentTodoEditBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val priorityList = arrayOf(
            SpannableString(getString(R.string.importance_none)),
            ImportanceTextModifyHelper.modifyText(
                getString(R.string.importance_low),
                TodoItemImportance.LOW,
                requireContext()
            ),
            ImportanceTextModifyHelper.modifyText(
                getString(R.string.importance_high),
                TodoItemImportance.HIGH,
                requireContext()
            )
        )

        val spinnerAdapter = TodoEditSpannableAdapter(
            view.context,
            R.layout.item_spinner,
            priorityList
        )

        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropped)

        viewBinding.spinner.adapter = spinnerAdapter
    }
}