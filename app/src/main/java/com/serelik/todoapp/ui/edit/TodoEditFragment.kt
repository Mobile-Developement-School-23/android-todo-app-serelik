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
import com.serelik.todoapp.ui.edit.compose.TodoEditScreen
import java.time.LocalDate
import javax.inject.Inject

class TodoEditFragment : Fragment(/*R.layout.fragment_todo_edit*/) {

    private val viewModel: TodoEditViewModel by viewModels {
        viewModelFactory
    }

    lateinit var component: TodoEditFragmentComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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

        fun createFragment(id: String?): TodoEditFragment {
            val arg = Bundle()
            arg.putString(EDIT_ID_KEY, id)
            val fragment = TodoEditFragment()
            fragment.arguments = arg
            return fragment
        }
    }
}
