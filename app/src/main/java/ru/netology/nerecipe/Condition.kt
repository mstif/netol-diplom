package ru.netology.nerecipe

data class Condition(
    val id: String = "default",
    val maxIdStages: Long = 1L,
    val filterCategory: String = ""
)

