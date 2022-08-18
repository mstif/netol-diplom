package ru.netology.nerecipe

data class FilterFeed(
    val searchText: String,
    val categories: List<Int>
)