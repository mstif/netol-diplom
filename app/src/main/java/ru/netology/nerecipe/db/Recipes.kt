package ru.netology.nerecipe.db

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.RecipeCategory


fun RecipeEntity.toModel() = Recipe(
    id = id,
    describe = describe,
    author = author,
    photoRecipe = photoRecipe,
    stages = Json.decodeFromString(stages),
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