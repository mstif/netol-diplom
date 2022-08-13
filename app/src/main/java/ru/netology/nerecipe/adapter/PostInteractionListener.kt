package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.Recipe

interface PostInteractionListener {
    fun onLikeClicked(recipe: Recipe)
    fun onDeleteClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onNavigateClicked(recipe: Recipe)

}