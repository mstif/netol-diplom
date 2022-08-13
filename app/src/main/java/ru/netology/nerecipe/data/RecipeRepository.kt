package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nerecipe.Recipe

import x.y.z.SingleLiveEvent

interface RecipeRepository {
    val data: LiveData< List<Recipe>>
    val sharePostContent:SingleLiveEvent<String>
    val currentRecipe:MutableLiveData<Recipe?>
   // val currentSinglePost:MutableLiveData<Post?>
    fun like(id:Long)
    fun delete(id: Long)
    fun save(recipe: Recipe)
    fun getRecipeById(id:Long): Recipe?

    companion object{
        const val NEW_RECIPE_ID = 0L
    }
}