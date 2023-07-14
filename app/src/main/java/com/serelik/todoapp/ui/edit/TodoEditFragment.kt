package com.serelik.todoapp.ui.edit

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.serelik.todoapp.R
import com.serelik.todoapp.di.TodoEditFragmentComponent
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.ui.MainActivity
import java.time.LocalDate
import javax.inject.Inject

class TodoEditFragment : Fragment(/*R.layout.fragment_todo_edit*/) {

    private val viewModel: TodoEditViewModel by viewModels {
        viewModelFactory
    }

    lateinit var component: TodoEditFragmentComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /*    private val binding by viewBinding(FragmentTodoEditBinding::bind)*/

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    private val itemId by lazy { arguments?.getString(EDIT_ID_KEY) ?: TodoItem.NEW_TODO_ID }

    private var dateSetListener =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            val deadline = LocalDate.of(year, monthOfYear, dayOfMonth)
            viewModel.setNewDeadline(deadline)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = (requireActivity() as MainActivity)
            .activityComponent
            .todoEditFragmentComponent().create()

        component.inject(this)

        viewModel.loadTodoItem(itemId)

        ImportanceBottomSheetFragment.setFragmentResult(this, viewModel::setNewImportance)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.screenState.collectAsState()
                TodoEditScreen(
                    todoEditScreenState = uiState,
                    onDeadlineChangeState = ::switchState,
                    onBackClick = ::navigateBack,
                    onImportanceClick = ::showBottomSheet,
                    onDeleteClick = ::remove,
                    onChangeText = viewModel::updateTodoItem,
                    onSaveButtonClick = ::save
                )
            }
        }
    }

    private fun showDatePicker() {
        val deadLine = viewModel.screenState.value.deadlineDate ?: LocalDate.now()

        val dialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            deadLine.year,
            deadLine.monthValue,
            deadLine.dayOfMonth
        )
        dialog.setOnCancelListener { viewModel.setNewDeadline(null) }
        dialog.show()
    }

    private fun showBottomSheet() {
        ImportanceBottomSheetFragment().show(supportFragmentManager, "bottom sheet")
    }

    /*    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            setupSpinner()
            setupSwitch()
            setupToolbar()

            binding.textViewDeadlineDate.setOnClickListener { showDatePicker() }

            viewModel.todoItem.observe(viewLifecycleOwner, ::bindTodo)

            if (itemId != TodoItem.NEW_TODO_ID) {
                binding.textViewDelete.isEnabled = true
                binding.textViewDelete.setOnClickListener {
                    viewModel.remove()
                    supportFragmentManager.popBackStack()
                }
            }
        }



        private fun bindTodo(todoItem: TodoItem) {
            binding.apply {
                editText.setText(todoItem.text)
                spinner.setSelection(todoItem.importance.ordinal)

                if (todoItem.deadline != null) {
                    switchCompat.isChecked = true
                    textViewDeadlineDate.text = DateFormatterHelper.format(todoItem.deadline)
                    textViewDeadlineDate.isVisible = true
                } else {
                    textViewDeadlineDate.isVisible = false
                }
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
                if (!switchView.isPressed) {
                    return@setOnCheckedChangeListener
                }
                if (isChecked) {
                    showDatePicker()
                } else {
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
                        importance = TodoItemImportance.values()[binding.spinner.selectedItemPosition]
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
        }*/

    private fun navigateBack() {
        supportFragmentManager.popBackStack()
    }

    private fun remove() {
        if (!viewModel.screenState.value.isNew) {
            viewModel.remove()
            navigateBack()
        }
    }

    private fun save() {
        if (viewModel.screenState.value.text.isNotBlank()) {
            viewModel.save()
            navigateBack()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.empty_todo_edit_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun switchState(isSwitch: Boolean) {
        if (isSwitch) {
            showDatePicker()
        } else {
            viewModel.setNewDeadline(null)
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
