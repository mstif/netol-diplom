package ru.netology.nerecipe.data.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.data.RecipeRepository


import ru.netology.nerecipe.db.RecipeDao
import ru.netology.nerecipe.db.toEntity
import ru.netology.nerecipe.db.toModel

import x.y.z.SingleLiveEvent

class SqLiteRepository(private val dao: RecipeDao) : RecipeRepository {

//    override val data = dao.getAll().map { entities ->
//        entities.map { it.toModel() }
//    }
    override val data = MutableLiveData(Recipe.demoDataRecipe())

    override val sharePostContent = SingleLiveEvent<String>()
    override val currentRecipe = MutableLiveData<Recipe?>(null)


    override fun like(id: Long) {
        dao.toFavoriteById(id)

    }



    override fun delete(id: Long) {
        dao.removeById(id)
    }

    override fun save(recipe: Recipe) {

        dao.save(recipe.toEntity())

    }

    // object SingletonData
    //  { val currentPost = MutableLiveData<Post?>(null)
    // val data = MutableLiveData<List<Post>>(null)
    // }

    //private fun setCurrentPost(id: Long) {
    //    currentPost.value = data.value?.find { it.id == id }
    // }

    override fun getRecipeById(id: Long): Recipe {
        val p = dao.getRecipeById(id).toModel()
        return p
    }

}