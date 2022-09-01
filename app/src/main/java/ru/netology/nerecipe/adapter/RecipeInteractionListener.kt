package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.FilterFeed
import ru.netology.nerecipe.Recipe

interface RecipeInteractionListener {
    fun onLikeClicked(recipe: Recipe)
    fun onDeleteClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onNavigateClicked(recipe: Recipe)
    fun onChangeFilters(filter: FilterFeed?)

}