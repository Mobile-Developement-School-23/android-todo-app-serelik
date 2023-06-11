package com.serelik.todoapp.list

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.FragmentTodoListBinding
import com.serelik.todoapp.repository.TodoItemsRepository

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    val todoItemAdapter = TodoItemAdapter()

    private val viewBinding by viewBinding(FragmentTodoListBinding::bind)

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
}