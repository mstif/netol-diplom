package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.FilterFeed
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Stage

interface StageInteractionListener {

    fun onDeleteClicked(stage: Stage)
    fun onEditClicked(stage: Stage)






}