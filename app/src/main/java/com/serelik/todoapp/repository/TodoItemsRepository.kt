package com.serelik.todoapp.repository

import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import java.time.LocalDate

object TodoItemsRepository {

    fun getItems() = listOf(
        TodoItem(
            id = "1",
            text = "First todo",
            created = LocalDate.now()
        ),
        TodoItem(
            id = "2",
            text = "Second todo",
            deadline = LocalDate.of(2001, 8, 22),
            importance = TodoItemImportance.LOW,
            created = LocalDate.of(2001, 8, 20)
        ),
        TodoItem(
            id = "3",
            text = "Certain but she but shyness why cottage. Gay the put instrument sir entreaties affronting. Pretended exquisite see cordially the you. Weeks quiet do vexed or whose. Motionless if no to affronting imprudence no precaution. My indulged as disposal strongly attended. Parlors men express had private village man. Discovery moonlight recommend all one not. Indulged to answered prospect it bachelor is he bringing shutters. Pronounce forfeited mr direction oh he dashwoods ye unwilling.",
            importance = TodoItemImportance.HIGH,
            isDone = true,
            created = LocalDate.of(2050, 6, 2),
            deadline = LocalDate.of(2051, 8, 22),
        ),
        TodoItem(
            id = "4",
            text = "Fourth todo",
            importance = TodoItemImportance.HIGH,
            created = LocalDate.of(2071, 8, 20)
        )
    )
}