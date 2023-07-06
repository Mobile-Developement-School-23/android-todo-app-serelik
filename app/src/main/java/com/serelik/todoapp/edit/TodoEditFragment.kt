package com.serelik.todoapp.edit

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.DateFormatterHelper
import com.serelik.todoapp.ImportanceTextModifyHelper
import com.serelik.todoapp.MainActivity
import com.serelik.todoapp.R
import com.serelik.todoapp.compoment
import com.serelik.todoapp.databinding.FragmentTodoEditBinding
import com.serelik.todoapp.di.MultiViewModelFactory
import com.serelik.todoapp.di.TodoEditFragmentComponent
import com.serelik.todoapp.di.TodoListFragmentComponent
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import dagger.Lazy
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider

class TodoEditFragment : Fragment(R.layout.fragment_todo_edit) {

    private val viewModel: TodoEditViewModel by viewModels() {
        viewModelFactory
    }

    lateinit var component: TodoEditFragmentComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val binding by viewBinding(FragmentTodoEditBinding::bind)

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    private val itemId by lazy { arguments?.getString(EDIT_ID_KEY) ?: "" }

    private var dateSetListener =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            binding.textViewDeadlineDate.isVisible = true

            val deadline = LocalDate.of(year, monthOfYear, dayOfMonth)
            viewModel.newDeadline = deadline
            binding.textViewDeadlineDate.text = DateFormatterHelper.format(deadline)

        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        component = (requireActivity() as MainActivity)
            .activityComponent
            .todoEditFragmentComponent().create()

        component.inject(this)

        viewModel.loadTodoItem(itemId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupSwitch()
        setupToolbar()

        binding.textViewDeadlineDate.setOnClickListener { showDatePicker() }

        viewModel.todoItemLiveData.observe(viewLifecycleOwner, ::bindTodo)

        if (itemId != "-1") {
            binding.textViewDelete.isEnabled = true
            binding.textViewDelete.setOnClickListener {
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
        dialog.setOnCancelListener { binding.switchCompat.isChecked = false }
        dialog.show()
    }

    private fun bindTodo(todoItem: TodoItem) {

        binding.apply {
            editText.setText(todoItem.text)
            spinner.setSelection(todoItem.importance.ordinal)

            if (todoItem.deadline != null) {
                switchCompat.isChecked = true
                textViewDeadlineDate.text = DateFormatterHelper.format(todoItem.deadline)
                textViewDeadlineDate.isVisible = true
            } else textViewDeadlineDate.isVisible = false
        }
    }

    private fun setupSpinner() {
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
            requireContext(),
            R.layout.item_spinner,
            priorityList
        )

        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropped)

        binding.spinner.adapter = spinnerAdapter
    }

    private fun setupSwitch() {
        binding.switchCompat.setOnCheckedChangeListener { switchView, isChecked ->
            if (!switchView.isPressed)
                return@setOnCheckedChangeListener
            if (isChecked)
                showDatePicker()
            else {
                binding.textViewDeadlineDate.text = null
                viewModel.newDeadline = null
                binding.textViewDeadlineDate.isVisible = false
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }

        binding.toolbar.menu.findItem(R.id.action_save).setOnMenuItemClickListener {
            if (binding.editText.text.isNotBlank()) {
                viewModel.save(
                    text = binding.editText.text.toString(),
                    importance = TodoItemImportance.values()[binding.spinner.selectedItemPosition],
                )
                supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_todo_edit_error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
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