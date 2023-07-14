package com.serelik.todoapp.ui.edit

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.BottomSheetImportanceBinding
import com.serelik.todoapp.model.TodoItemImportance
import com.serelik.todoapp.ui.ImportanceTextModifyHelper

class ImportanceBottomSheetFragment : BottomSheetDialogFragment(R.layout.bottom_sheet_importance) {
    private val binding by viewBinding(BottomSheetImportanceBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textModifyForImportanceLow()
        textModifyForImportanceHigh()

        binding.importanceNone.setOnClickListener {
            setFragmentResult(TodoItemImportance.NONE)
        }

        binding.importanceLow.setOnClickListener {
            setFragmentResult(TodoItemImportance.LOW)
        }

        binding.importanceHigh.setOnClickListener {
            setFragmentResult(TodoItemImportance.HIGH)
        }
    }

    private fun setFragmentResult(result: TodoItemImportance) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(EXTRA_KEY to result.name)
        )
        dismiss()
    }

    private fun textModifyForImportanceLow() {
        val modifiedText = ImportanceTextModifyHelper.modifyText(
            getString(R.string.importance_low),
            TodoItemImportance.LOW,
            requireContext()
        )


        binding.importanceLow.text = modifiedText

    }

    private fun textModifyForImportanceHigh() {
        val modifiedText = ImportanceTextModifyHelper.modifyText(
            getString(R.string.importance_high),
            TodoItemImportance.HIGH,
            requireContext()
        )

        binding.importanceHigh.text = modifiedText


    }

    companion object {
        const val REQUEST_KEY = "REQUEST_KEY"
        const val EXTRA_KEY = "EXTRA_KEY"

        fun setFragmentResult(fragment: Fragment, action: (importance: TodoItemImportance) -> Unit) {
            fragment.setFragmentResultListener(REQUEST_KEY) { key, bundle ->
                val importanceName =
                    bundle.getString(EXTRA_KEY) ?: return@setFragmentResultListener
                val importance = TodoItemImportance.valueOf(importanceName)
                action(importance)
            }
        }
    }
}