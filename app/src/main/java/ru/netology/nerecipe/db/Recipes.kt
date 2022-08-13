package ru.netology.nerecipe.db

import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.RecipeCategory


fun RecipeEntity.toModel() = Recipe(
    id = id,
    describe = describe,
    author = author,
    photoRecipe = photoRecipe,
    stages = stages,
    favorites = favorites,
    category = RecipeCategory.values().find { it.categoryRus==category }
)


fun Recipe.toEntity() = RecipeEntity(
    id = id,
    describe = describe,
    author = author,
    photoRecipe = photoRecipe,
    stages = stages,
    favorites = favorites,
    category = category?.categoryRus?:""
)