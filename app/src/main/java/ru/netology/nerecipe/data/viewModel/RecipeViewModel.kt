package ru.netology.nerecipe.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nerecipe.FilterFeed

import ru.netology.nerecipe.Recipe

import ru.netology.nerecipe.adapter.RecipeInteractionListener


import ru.netology.nerecipe.data.impl.SqLiteRepository
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.data.RecipeRepository

import x.y.z.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.*

class RecipeViewModel(application: Application) : AndroidViewModel(application),
    RecipeInteractionListener {

    private val repository: RecipeRepository = SqLiteRepository(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )
    val dataViewModel by repository::data
    val sharePostContentModel by repository::sharePostContent
    val navigateToRecipeScreenEvent = SingleLiveEvent<String>()
    val navigateToRecipeSingle = SingleLiveEvent<Recipe>()
    val filter = SingleLiveEvent<FilterFeed>()
    var listAllCategories = listOf<String>()

    val currentRecipe by repository::currentRecipe

    override fun onLikeClicked(recipe: Recipe) = repository.like(recipe.id)


    override fun onDeleteClicked(recipe: Recipe) = repository.delete(recipe.id)
    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeScreenEvent.value = recipe.describe

    }


    override fun onNavigateClicked(recipe: Recipe) {
        navigateToRecipeSingle.value = recipe
    }

    override fun onChangeFilters(filter: FilterFeed?) {
        this.filter.value = filter
    }

    fun getRecipeById(id: Long): Recipe? = repository.getRecipeById(id)

    fun onSaveButtonClicked(describe: String) {
        if (describe.isBlank()) return
        val url = getVideoUrl(describe)
        val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault())

        val editedRecipe =
            currentRecipe.value?.copy(describe = describe, photoRecipe = url) ?: Recipe(
                id = RecipeRepository.NEW_RECIPE_ID,
                describe = describe,
                author = "Me",
                photoRecipe = url
            )
        currentRecipe.value = editedRecipe
        repository.save(editedRecipe)
        // currentPost.value = null
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
        navigateToRecipeScreenEvent.call()
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


    fun agoToText(countSec: Int): String {
        return when (countSec) {
            in 0..60 -> "только что"
            in 61..60 * 60 -> {
                "${periodToString(countSec / 60, TimeUnit.Minutes)} назад"
            }
            in 60 * 60 + 1..24 * 60 * 60 -> {
                "${periodToString(countSec / (60 * 60), TimeUnit.Hours)} назад"
            }
            in 24 * 60 * 60 + 1..48 * 60 * 60 -> {
                "сегодня"
            }
            in 48 * 60 * 60 + 1..72 * 60 * 60 -> {
                "вчера"
            }
            else -> "давно"
        }
    }

    enum class TimeUnit {
        Minutes,
        Hours
    }


    private fun periodToString(countUnits: Int, unit: TimeUnit): String {
        val lastDigit: Int = countUnits % 10
        return "$countUnits " + when (unit) {
            TimeUnit.Minutes -> when (lastDigit) {
                0, 5, 6, 7, 8, 9 -> "минут"
                1 -> "минута"
                2, 3, 4 -> "минуты"
                else -> ""
            }
            TimeUnit.Hours -> when (lastDigit) {
                0, 5, 6, 7, 8, 9 -> "часов"
                1 -> "час"
                2, 3, 4 -> "часа"
                else -> ""
            }

        }
    }
}