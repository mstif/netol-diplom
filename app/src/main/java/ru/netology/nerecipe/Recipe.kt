package ru.netology.nerecipe

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long = 0,
    val describe: String = "",
    val author: String = "",
    val favorites: Boolean = false,
    val stages: List<Stage> = listOf(),
    val category: String = "",
    val photoRecipe: String = "",
    val indexOrder: Long = 0
) {
    companion object {
        fun demoDataRecipe(): List<Recipe> {
            return List(10) { index ->

                Recipe(
                    id = index.toLong(),
                    describe = "$index Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                    author = "$index Всемирная ассоциация любителей поесть",
                    favorites = false,

                   // photoRecipe = "content://com.android.providers.media.documents/document/image%3A27",
                    category = "Russian",
                    indexOrder = index.toLong()
                )


            } + List(10) { index ->

                Recipe(
                    id = 10 + index.toLong(),
                    describe = "$index Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                    author = "$index Всемирная ассоциация любителей поесть",
                    favorites = false,

                    photoRecipe = "content://com.android.providers.media.documents/document/image%3A27",
                    category = "Eastern",
                    indexOrder = 10 + index.toLong()
                )


            }


        }
    }
}

