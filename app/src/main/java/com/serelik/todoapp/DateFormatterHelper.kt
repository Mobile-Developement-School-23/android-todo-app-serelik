package com.serelik.todoapp

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateFormatterHelper {
    private val dateFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern("d MMMM yyyy") }

    fun format(localDate: LocalDate): String {
        return localDate.format(dateFormatter)
    }
}