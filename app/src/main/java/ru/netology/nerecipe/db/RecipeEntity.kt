package ru.netology.nerecipe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nerecipe.RecipeCategory


@Entity(tableName="recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long ,
    val describe: String ,
    val author: String ,
    @ColumnInfo(name = "favorites")
    val favorites: Boolean ,
    val stages:String,
    val photoRecipe: String,
    val category: String,
    val indexOrder :Long
)

class StageEntity