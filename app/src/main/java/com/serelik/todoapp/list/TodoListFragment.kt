package com.serelik.todoapp.list

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.R
import com.serelik.todoapp.edit.TodoEditFragment
import com.serelik.todoapp.databinding.FragmentTodoListBinding
import com.serelik.todoapp.repository.TodoItemsRepository

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {
    private val todoItemAdapter = TodoItemAdapter(
        onTodoClickListener = ::openEditFragment
    )

    private val viewBinding by viewBinding(FragmentTodoListBinding::bind)

    val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = viewBinding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = todoItemAdapter

        todoItemAdapter.submitList(TodoItemsRepository.getItems())
        getVisibilityTodoItemButton()?.setOnMenuItemClickListener {
            updateVisibilityTodoDoneItemsStatus(true) // TODO
            true
        }

        viewBinding.floatingActionButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, TodoEditFragment())
                .addToBackStack("Todo add fragment")
                .commit()
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
            .addToBackStack("Edit_id_Key")
            .commit()
    }
}