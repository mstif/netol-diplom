package ru.netology.nerecipe.db

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nerecipe.Condition

import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.RecipeCategory
import ru.netology.nerecipe.Stage


fun RecipeEntity.toModel() = Recipe(
    id = id,
    describe = describe,
    author = author,
    photoRecipe = photoRecipe,
    //stages = listOf<Stage>(),
    stages = if(stages.isNullOrBlank()) listOf<Stage>() else Json.decodeFromString(stages),
    favorites = favorites,
    category = category,
    indexOrder = indexOrder
)


fun Recipe.toEntity() = RecipeEntity(
    id = id,
    describe = describe,
    author = author,
    photoRecipe = photoRecipe,
    stages = Json.encodeToString(stages),
    favorites = favorites,
    category = category,
    indexOrder = indexOrder
)

fun Condition.toEntity() = ConditionEntity(
    id = id,
    maxIdStages = maxIdStages,
    filterCategory = filterCategory
)


fun ConditionEntity.toModel() = Condition(
    id = id,
    maxIdStages = maxIdStages,
    filterCategory = filterCategory
)