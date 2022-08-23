package ru.netology.nerecipe

data class Condition(
    val id:String = "default",
    val maxIdStages : Long = 0L,
    val filterCategory: String = ""
)

