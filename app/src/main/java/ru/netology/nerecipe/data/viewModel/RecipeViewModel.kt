package ru.netology.nerecipe.data.viewModel

import android.app.Application
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
    RecipeInteractionListener,StageInteractionListener {

    private val repository: RecipeRepository = SqLiteRepository(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )
    val dataViewModel by repository::data
    val sharePostContentModel by repository::sharePostContent
    val navigateToRecipeScreenEvent = SingleLiveEvent<Recipe?>()
    val navigateToRecipeSingle = SingleLiveEvent<Recipe>()
    val filter = SingleLiveEvent<FilterFeed>()
    var listAllCategories = listOf<String>()

    val dataStages by repository::stages
    //val dataStages = repository.stages
    val navigateToStageScreenEvent=SingleLiveEvent<Stage?>()
    val currentRecipe by repository::currentRecipe
    val currentStage by repository::currentStage
    override fun onLikeClicked(recipe: Recipe) = repository.like(recipe.id)


    override fun onDeleteClicked(recipe: Recipe) = repository.delete(recipe.id)
    override fun onEditClicked(recipe: Recipe) {
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
        if(recipes==null) return filtered
        if (!(searchText==null || searchText.isBlank())) {
            filtered.addAll( recipes.filter { it.describe.lowercase().contains(searchText.lowercase())
                    && searchCategory?.contains(it.category)?:true
            })
        }else{
            if(searchCategory!=null && !searchCategory.isEmpty())
                filtered.addAll(recipes.filter { searchCategory.contains(it.category)})
            else return recipes
        }

        return filtered
    }

    fun getFilteredResultNew(
       // recipes: List<Recipe>?

    ): List<Recipe> {
        val filtered: MutableList<Recipe> = mutableListOf()
        val recipes = dataViewModel.value
        if(recipes==null) return filtered
        val searchText = filter.value?.searchText
        val searchCategory = filter.value?.categories
        if (!(searchText==null || searchText.isBlank())) {
            filtered.addAll( recipes.filter { it.describe.lowercase().contains(searchText.lowercase())
                    && searchCategory?.contains(listAllCategories.indexOf(it.category))?:true
            })
        }else{
            if(searchCategory!=null && !searchCategory.isEmpty())
                filtered.addAll(recipes.filter { searchCategory.contains(listAllCategories.indexOf(it.category))})
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

    override fun onSetImage(uri : String){
        currentStage.value = currentStage.value?.copy(photo = uri)
    }


    fun onMoveItem(to: Int, from: Int, recipeToId: Long, recipeFromId: Long){
        repository.onMoveItem(to,from,recipeToId,recipeFromId)
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

    override fun onLikeClicked(stage: Stage) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClicked(stage: Stage) {
        TODO("Not yet implemented")
    }

    override fun onEditClicked(stage: Stage) {
        TODO("Not yet implemented")
    }

    override fun onNavigateClicked(stage: Stage) {
        TODO("Not yet implemented")
    }


}