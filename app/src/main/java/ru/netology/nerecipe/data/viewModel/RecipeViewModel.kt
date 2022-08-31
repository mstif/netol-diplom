package ru.netology.nerecipe.data.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import ru.netology.nerecipe.FilterFeed

import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Stage

import ru.netology.nerecipe.adapter.RecipeInteractionListener
import ru.netology.nerecipe.adapter.StageInteractionListener


import ru.netology.nerecipe.data.impl.SqLiteRepository
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.data.RecipeRepository

import x.y.z.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.*

class RecipeViewModel(application: Application) : AndroidViewModel(application),
    RecipeInteractionListener, StageInteractionListener {

    private val repository: RecipeRepository = SqLiteRepository(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )
    val dataViewModel by repository::data
    val navigateToRecipeScreenEvent = SingleLiveEvent<Recipe?>()
    val navigateToRecipeSingle = SingleLiveEvent<Recipe>()
    val filter = SingleLiveEvent<FilterFeed>()
    var listAllCategories = listOf<String>()

    val dataStages by repository::stages

    val navigateToStageScreenEvent = SingleLiveEvent<Stage?>()
    val currentRecipe by repository::currentRecipe
    val currentStage by repository::currentStage
    override fun onLikeClicked(recipe: Recipe) = repository.like(recipe.id)


    override fun onDeleteClicked(recipe: Recipe) = repository.delete(recipe.id)
    override fun onEditClicked(recipe: Recipe) {
        //val recipeEdit = getRecipeByIdFromLiveData(recipe.id)

        currentRecipe.value = recipe
        navigateToRecipeScreenEvent.value = recipe


    }


    override fun onNavigateClicked(recipe: Recipe) {
        navigateToRecipeSingle.value = recipe
    }

    override fun onChangeFilters(filter: FilterFeed?) {
        this.filter.value = filter
    }

    fun getRecipeById(id: Long): Recipe? = repository.getRecipeById(id)

    fun getRecipeByIdFromLiveData(id: Long?): Recipe? = dataViewModel.value?.find { it.id == id }

    fun onSaveButtonClicked() {

        val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault())

        val editedRecipe =
            currentRecipe.value?.copy(author = "Me") ?: Recipe(
                id = RecipeRepository.NEW_RECIPE_ID,

                author = "Me",

                )
        currentRecipe.value = editedRecipe
        repository.save(editedRecipe)


    }

    fun getFilteredResult(
        recipes: List<Recipe>?,
        searchText: String?,
        searchCategory: Array<String>?
    ): List<Recipe> {
        val filtered: MutableList<Recipe> = mutableListOf()
        if (recipes == null) return filtered
        if (!(searchText == null || searchText.isBlank())) {
            filtered.addAll(recipes.filter {
                it.describe.lowercase().contains(searchText.lowercase())
                        && searchCategory?.contains(it.category) ?: true
            })
        } else {
            if (searchCategory != null && !searchCategory.isEmpty())
                filtered.addAll(recipes.filter { searchCategory.contains(it.category) })
            else return recipes
        }

        return filtered
    }

    fun getFilteredResultNew(): List<Recipe> {
        val filtered: MutableList<Recipe> = mutableListOf()
        val recipes = dataViewModel.value
        if (recipes == null) return filtered
        val searchText = filter.value?.searchText
        val searchCategory = filter.value?.categories
        if (!(searchText == null || searchText.isBlank())) {
            filtered.addAll(recipes.filter {
                it.describe.lowercase().contains(searchText.lowercase())
                        && searchCategory?.contains(listAllCategories.indexOf(it.category)) ?: true
            })
        } else {
            if (searchCategory != null && !searchCategory.isEmpty())
                filtered.addAll(recipes.filter {
                    searchCategory.contains(
                        listAllCategories.indexOf(
                            it.category
                        )
                    )
                })
            else return recipes
        }

        return filtered
    }


    fun onAddClicked() {
        currentRecipe.value = null

        navigateToRecipeScreenEvent.value = null

        //navigateToRecipeScreenEvent.call()
    }

    fun onAddStageClicked() {
        currentStage.value = Stage()
        navigateToStageScreenEvent.value = null

        //navigateToRecipeScreenEvent.call()
    }

    override fun onSetImage(uri: String) {
        currentStage.value = currentStage.value?.copy(photo = uri)
    }

    fun nextIdStages(): Long {
        return repository.nextIdStages()

    }


    fun onMoveItem(to: Int, from: Int, recipeToId: Long, recipeFromId: Long) {
        repository.onMoveItem(to, from, recipeToId, recipeFromId)
        // println("to="+to+" from="+from + " stageTo="+recipeToId + " stageFrom="+recipeFromId)
    }

    fun onMoveItemStage(to: Int, from: Int, stageTo: Stage, stageFrom: Stage) {
        // val list = dataStages.value
        val list = currentRecipe.value?.stages
        if (list == null) return
        val fromStage = list.find { it.id == stageFrom.id }
        val toStage = list.find { it.id == stageTo.id }

        val fromLocation = fromStage?.copy(position = fromStage.position + if (to < from) -1 else 1)
        val toLocation = toStage?.copy(position = toStage.position + if (to < from) 1 else -1)

        //list.removeAt(from)
        val res = list.map {
            if (it.id == fromLocation?.id) fromLocation
            else
                if (it.id == toLocation?.id) toLocation
                else it
        }
        val recipe = currentRecipe.value?.copy(stages = res)
        currentRecipe.value = recipe

        dataStages.value = res
        //onSaveButtonClicked()


    }


    private fun getVideoUrl(content: String): String {
        val startIndex = content.indexOf("http")
        if (startIndex > -1) {
            var endIndex = content.indexOf(" ", startIndex)
            if (endIndex == -1) {
                endIndex = content.indexOf('\n', startIndex)
            }
            return content.substring(startIndex, if (endIndex > -1) endIndex else content.length)

        }
        return ""
    }


    override fun onDeleteClicked(stage: Stage) {
        val stages = currentRecipe.value?.stages?.toMutableList()
        stages?.removeAll { it.id == stage.id }
        val recipe = currentRecipe.value?.copy(stages = stages ?: listOf())
        currentRecipe.value = recipe
        dataStages.value = stages
        //onSaveButtonClicked()

    }

    override fun onEditClicked(stage: Stage) {
        currentStage.value = stage
        navigateToStageScreenEvent.value = stage

    }

    override fun onNavigateClicked(stage: Stage) {
        TODO("Not yet implemented")
    }


}