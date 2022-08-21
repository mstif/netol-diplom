package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.FilterFeed
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Stage

interface StageInteractionListener {
    fun onLikeClicked(stage: Stage)
    fun onDeleteClicked(stage: Stage)
    fun onEditClicked(stage: Stage)
    fun onNavigateClicked(stage: Stage)
    fun onSetImage(uri : String)



}