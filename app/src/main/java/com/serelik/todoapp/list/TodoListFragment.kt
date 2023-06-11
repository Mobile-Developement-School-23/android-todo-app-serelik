package com.serelik.todoapp.list

import android.os.Bundle
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


    }
}