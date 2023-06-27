package com.serelik.todoapp.data.local.repository

import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import com.serelik.todoapp.model.TodoListScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.LocalDateTime

object TodoItemsRepositoryMock {

    private val list by lazy {
        mutableListOf(
            TodoItem(
                id = "0",
                text = "First todo",
                ),
            TodoItem(
                id = "1",
                text = "Second todo",
                deadline = LocalDate.of(2001, 8, 22),
                importance = TodoItemImportance.LOW,
                ),
            TodoItem(
                id = "2",
                text = "Certain but she but shyness why cottage. Gay the put instrument sir entreaties affronting. Pretended exquisite see cordially the you. Weeks quiet do vexed or whose. Motionless if no to affronting imprudence no precaution. My indulged as disposal strongly attended. Parlors men express had private village man. Discovery moonlight recommend all one not. Indulged to answered prospect it bachelor is he bringing shutters. Pronounce forfeited mr direction oh he dashwoods ye unwilling.",
                importance = TodoItemImportance.HIGH,
                isDone = true,

                deadline = LocalDate.of(2051, 8, 22),
            ),
            TodoItem(
                id = "3",
                text = "Fourth todo",
                importance = TodoItemImportance.HIGH,

                ),
            TodoItem(
                id = "4",
                text = "First todo",

                ),
            TodoItem(
                id = "5",
                text = "Second todo",
                deadline = LocalDate.of(2001, 8, 2),
                importance = TodoItemImportance.LOW,

                ),
            TodoItem(
                id = "6",
                text = "Certain but she but shyness why cottage. Gay the put instrument sir entreaties affronting. Pretended exquisite see cordially the you. Weeks quiet do vexed or whose. Motionless if no to affronting imprudence no precaution. My indulged as disposal strongly attended. Parlors men express had private village man. Discovery moonlight recommend all one not. Indulged to answered prospect it bachelor is he bringing shutters. Pronounce forfeited mr direction oh he dashwoods ye unwilling.",
                importance = TodoItemImportance.HIGH,
                isDone = true,

                deadline = LocalDate.of(9999, 9, 9),
            ),
            TodoItem(
                id = "7",
                text = "Fourth todo",
                importance = TodoItemImportance.HIGH,

                deadline = LocalDate.of(1212, 12, 31)
            ),
            TodoItem(
                id = "8",
                text = "First todo",

                ),
            TodoItem(
                id = "9",
                text = "Second todo",
                deadline = LocalDate.of(2001, 8, 22),
                importance = TodoItemImportance.LOW,

                ),
            TodoItem(
                id = "10",
                text = "Certain but she but shyness why cottage. Gay the put instrument sir entreaties affronting. Pretended exquisite see cordially the you. Weeks quiet do vexed or whose. Motionless if no to affronting imprudence no precaution. My indulged as disposal strongly attended. Parlors men express had private village man. Discovery moonlight recommend all one not. Indulged to answered prospect it bachelor is he bringing shutters. Pronounce forfeited mr direction oh he dashwoods ye unwilling.",
                importance = TodoItemImportance.HIGH,
                isDone = true,

                deadline = LocalDate.of(2051, 8, 22),
            ),
            TodoItem(
                id = "11",
                text = "Fourth todo",
                importance = TodoItemImportance.HIGH,

                ),
            TodoItem(
                id = "12",
                text = "First todo",

                ),
            TodoItem(
                id = "13",
                text = "Second todo",
                deadline = LocalDate.of(2001, 8, 22),
                importance = TodoItemImportance.LOW,

                ),
            TodoItem(
                id = "14",
                text = "Certain but she but shyness why cottage. Gay the put instrument sir entreaties affronting. Pretended exquisite see cordially the you. Weeks quiet do vexed or whose. Motionless if no to affronting imprudence no precaution. My indulged as disposal strongly attended. Parlors men express had private village man. Discovery moonlight recommend all one not. Indulged to answered prospect it bachelor is he bringing shutters. Pronounce forfeited mr direction oh he dashwoods ye unwilling.",
                importance = TodoItemImportance.HIGH,
                isDone = true,

                deadline = LocalDate.of(2051, 8, 22),
            ),
            TodoItem(
                id = "15",
                text = "Fourth todo",
                importance = TodoItemImportance.HIGH,
                modified = null,
                isDone = true
            ),
        )
    }

    var isDoneVisible = true

    private val todoItemsStateFlow = MutableStateFlow(createTodoListScreenModel())

    private fun addTodo(todoItem: TodoItem) {
        list.add(todoItem.copy(id = list.size.toString(), created = LocalDateTime.now()))
    }

    fun removeTodo(id: String) {
        list.removeIf {
            it.id == id
        }
        updateListFlow()
    }

    fun updateTodo(todoItem: TodoItem) {
        val index = list.indexOfFirst { it.id == todoItem.id }
        if (index != -1)
            list[index] = todoItem.copy(modified = LocalDateTime.now())
        else
            addTodo(todoItem)
        updateListFlow()
    }

    fun loadTodo(id: String): TodoItem? {
        return list.find { it.id == id }
    }

    fun changedStateDone(itemId: String, isDone: Boolean) {
        val index = list.indexOfFirst { it.id == itemId }

        if (index == -1)
            return

        val todoItem = list[index]

        list[index] = todoItem.copy(isDone = isDone)
        updateListFlow()
    }

    private fun updateListFlow() {
        todoItemsStateFlow.value = createTodoListScreenModel()
    }

    fun changeDoneVisibility() {
        isDoneVisible = !isDoneVisible
        updateListFlow()

    }

    private fun createTodoListScreenModel(): TodoListScreenModel {
        val newList = if (isDoneVisible) {
            list.toList()
        } else
            list.filter { !it.isDone }
        val doneCount = list.count { it.isDone }
        return TodoListScreenModel(newList, isDoneVisible, doneCount)
    }

}