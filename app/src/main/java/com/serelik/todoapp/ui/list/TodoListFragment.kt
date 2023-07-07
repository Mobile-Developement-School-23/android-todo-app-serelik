package com.serelik.todoapp.ui.list

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.FragmentTodoListBinding
import com.serelik.todoapp.di.TodoListFragmentComponent
import com.serelik.todoapp.model.TodoListScreenModel
import com.serelik.todoapp.ui.MainActivity
import com.serelik.todoapp.ui.edit.TodoEditFragment
import java.io.IOException
import javax.inject.Inject

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private val viewModel: TodoListViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var component: TodoListFragmentComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val todoItemAdapter by lazy {
        TodoItemAdapter(
            onTodoClickListener = ::openEditFragment,
            changeIsDoneListener = viewModel::changedStateDone,
            onNewTodoClickListener = ::openAddFragment
        )
    }

    private val viewBinding by viewBinding(FragmentTodoListBinding::bind)

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    private val onSwipeTodo = object : OnSwipeTodo {
        override fun onSwipeDone(position: Int) {
            val item = todoItemAdapter.getItemTodo(position) ?: return
            viewModel.changedStateDone(item, !item.isDone)
        }

        override fun onSwipeDelete(position: Int) {
            val item = todoItemAdapter.getItemTodo(position) ?: return
            viewModel.remove(item.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = (requireActivity() as MainActivity)
            .activityComponent
            .todoListFragmentComponent().create()

        component.inject(this)

        viewModel.changeDoneVisibility()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.todoListScreenModel.observe(viewLifecycleOwner, ::bindScreenModel)
        viewModel.loadingState.observe(viewLifecycleOwner, ::bindStatus)

        viewBinding.recyclerView.adapter = todoItemAdapter
        setupSwipeFunctionality()

        getVisibilityTodoItemButton()?.setOnMenuItemClickListener {
            viewModel.changeDoneVisibility()
            true
        }
        viewBinding.floatingActionButton.setOnClickListener {
            openAddFragment()
        }

        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadListFromServer()
        }
    }

    private fun getVisibilityTodoItemButton(): MenuItem? {
        return viewBinding.toolbar.menu.findItem(R.id.action_hide)
    }

    private fun updateVisibilityTodoDoneItemsStatus(isDoneVisible: Boolean) {
        val notificationIcon = if (isDoneVisible) {
            R.drawable.ic_visibility_off
        } else {
            R.drawable.ic_visibility_on
        }
        getVisibilityTodoItemButton()?.setIcon(notificationIcon)
    }

    private fun openEditFragment(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, TodoEditFragment.createFragment(id))
            .addToBackStack(EDIT_ID_KEY)
            .commit()
    }

    private fun setupSwipeFunctionality() {
        val swipeHelperCallback = SwipeHelperCallback(requireContext(), onSwipeTodo)
        val swipeHelper = ItemTouchHelper(swipeHelperCallback)
        swipeHelper.attachToRecyclerView(viewBinding.recyclerView)
    }

    private fun openAddFragment() {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, TodoEditFragment())
            .addToBackStack(TODO_ADD_FRAGMENT)
            .commit()
    }

    private fun showError(throwable: Throwable) {
        val message = if (throwable is IOException) {
            getString(R.string.no_internet_message)
        } else {
            getString(R.string.something_gone_wrong_message)
        }

        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
        viewBinding.swipeRefreshLayout.isRefreshing = false
    }

    private fun bindStatus(status: LoadingStatus) {
        when (status) {
            is LoadingStatus.Error -> showError(status.throwable)
            LoadingStatus.Loading -> {
                viewBinding.swipeRefreshLayout.isRefreshing = true
            }

            LoadingStatus.Success -> viewBinding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun bindScreenModel(model: TodoListScreenModel) {
        todoItemAdapter.submitList(model.items)
        updateVisibilityTodoDoneItemsStatus(model.isDoneVisible)
        viewBinding.textViewDoneCount.text =
            getString(R.string.is_done_count, model.doneCount)
        viewBinding.textViewDoneCount.isInvisible = !model.isDoneVisible
    }

    companion object {
        const val EDIT_ID_KEY = "Edit_id_Key"
        const val TODO_ADD_FRAGMENT = "todo_add_fragment"
    }
}
