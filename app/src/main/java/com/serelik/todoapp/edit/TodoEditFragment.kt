package com.serelik.todoapp.edit

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.DateFormatterHelper
import com.serelik.todoapp.ImportanceTextModifyHelper
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.FragmentTodoEditBinding
import com.serelik.todoapp.model.TodoItemImportance
import java.time.LocalDate

class TodoEditFragment : Fragment(R.layout.fragment_todo_edit) {

    private val viewModel: TodoEditViewModel by viewModels()

    private val viewBinding by viewBinding(FragmentTodoEditBinding::bind)

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    private val itemId by lazy { arguments?.getString(EDIT_ID_KEY) ?: "-1" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getTodoItem(itemId)

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

        viewBinding.switchCompat.setOnCheckedChangeListener { switchView, isChecked ->
            if (!switchView.isPressed)
                return@setOnCheckedChangeListener
            if (isChecked)
                showDatePicker()
            else {
                viewBinding.textViewDeadlineDate.text = null
                viewBinding.textViewDeadlineDate.isVisible = false
            }
        }

        viewBinding.toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }

        viewBinding.toolbar.menu.findItem(R.id.action_save).setOnMenuItemClickListener {
            if (viewBinding.editText.text.isNotBlank()) {
                viewModel.save(
                    text = viewBinding.editText.text.toString(),
                    importance = TodoItemImportance.values()[viewBinding.spinner.selectedItemPosition],
                )
                supportFragmentManager.popBackStack()
            } else Toast.makeText(
                requireContext(),
                getString(R.string.empty_todo_edit_error_message),
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        viewBinding.textViewDeadlineDate.setOnClickListener { showDatePicker() }


        bindTodo()

        if (itemId != "-1") {
            viewBinding.textViewDelete.isEnabled = true
            viewBinding.textViewDelete.setOnClickListener {
                viewModel.remove()
                supportFragmentManager.popBackStack()
            }
        }
    }

    private fun showDatePicker() {
        val deadLine = viewModel.newDeadline ?: LocalDate.now()

        val dialog = DatePickerDialog(
            requireContext(), dateSetListener,
            deadLine.year,
            deadLine.monthValue,
            deadLine.dayOfMonth
        )
        dialog.setOnCancelListener { viewBinding.switchCompat.isChecked = false }
        dialog.show()
    }

    private fun bindTodo() {
        val currentTodoItem = viewModel.todoItem

        viewBinding.apply {
            editText.setText(currentTodoItem.text)
            spinner.setSelection(currentTodoItem.importance.ordinal)

            if (currentTodoItem.deadline != null) {
                switchCompat.isChecked = true
                textViewDeadlineDate.text = DateFormatterHelper.format(currentTodoItem.deadline)
                textViewDeadlineDate.isVisible = true
            } else textViewDeadlineDate.isVisible = false
        }
    }

    private var dateSetListener =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            viewBinding.textViewDeadlineDate.isVisible = true

            val deadline = LocalDate.of(year, monthOfYear, dayOfMonth)
            viewModel.newDeadline = deadline
            viewBinding.textViewDeadlineDate.text = DateFormatterHelper.format(deadline)

        }

    companion object {
        private const val EDIT_ID_KEY = "Edit_id_Key"

        fun createFragment(id: String): TodoEditFragment {
            val arg = Bundle()
            arg.putString(EDIT_ID_KEY, id)
            val fragment = TodoEditFragment()
            fragment.arguments = arg
            return fragment
        }
    }
}