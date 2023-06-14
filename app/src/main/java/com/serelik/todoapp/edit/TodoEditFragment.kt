package com.serelik.todoapp.edit

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.DateFormatterHelper
import com.serelik.todoapp.ImportanceTextModifyHelper
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.FragmentTodoEditBinding
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import com.serelik.todoapp.repository.TodoItemsRepository
import java.time.LocalDate
import java.util.Calendar

class TodoEditFragment : Fragment(R.layout.fragment_todo_edit) {
    private val viewBinding by viewBinding(FragmentTodoEditBinding::bind)

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    var newDeadline: LocalDate? = null

    var created: LocalDate? = null

    var dateAndTime: Calendar = Calendar.getInstance()

    val itemId by lazy { arguments?.getString(EDIT_ID_KEY) ?: "-1" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                setDate()
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
                TodoItemsRepository.updateTodo(
                    TodoItem(
                        id = itemId,
                        text = viewBinding.editText.text.toString(),
                        created = created ?: LocalDate.now(),
                        importance = TodoItemImportance.values()[viewBinding.spinner.selectedItemPosition],
                        deadline = newDeadline
                    )
                )
                supportFragmentManager.popBackStack()
            } else Toast.makeText(
                requireContext(),
                getString(R.string.empty_todo_edit_error_message),
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        if (itemId != "-1") {
            bindTodo(itemId)
            viewBinding.textViewDelete.isEnabled = true
            viewBinding.textViewDelete.setOnClickListener {
                TodoItemsRepository.removeTodo(itemId)
                supportFragmentManager.popBackStack()
            }
        }
    }

    private fun setDate() {
        val dialog = DatePickerDialog(
            requireContext(), dateSetListener,
            dateAndTime[Calendar.YEAR],
            dateAndTime[Calendar.MONTH],
            dateAndTime[Calendar.DAY_OF_MONTH]
        )
        dialog.setOnCancelListener { viewBinding.switchCompat.isChecked = false }
        dialog.show()
    }

    fun bindTodo(id: String) {
        val currentTodoItem = TodoItemsRepository.getTodo(id) ?: return

        created = currentTodoItem.created

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
            newDeadline = deadline
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