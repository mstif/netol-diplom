package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nerecipe.data.RecipeRepository

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id=:id")
    fun getRecipeById(id: Long): RecipeEntity


    @Insert
    fun insert(recipe: RecipeEntity)

    @Query("UPDATE recipes SET describe = :describe, photoRecipe = :photoRecipe, stages = :stages WHERE id = :id")
    fun updateRecipeById(id: Long, describe: String, photoRecipe: String, stages: String)


    fun save(recipe: RecipeEntity) =
        if (recipe.id == RecipeRepository.NEW_RECIPE_ID) insert(recipe) else
            updateRecipeById(recipe.id, recipe.describe, recipe.photoRecipe, recipe.stages)

    @Query(
        """
        UPDATE recipes SET favorites =   CASE WHEN favorites = 0 THEN 1 ELSE 0 END
        WHERE id= :id
    """
    )
    fun toFavoriteById(id: Long)

    @Query("DELETE FROM recipes WHERE id=:id")
    fun removeById(id: Long)


    @Query(
        """
        UPDATE recipes SET indexOrder =   indexOrder+:direction
        WHERE id= :idItem
    """
    )
    fun reorderItems(direction: Int, idItem: Long) {

    }


}