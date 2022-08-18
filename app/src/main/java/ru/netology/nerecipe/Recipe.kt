package ru.netology.nerecipe

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long = 0,
    val describe: String = "",
    val author: String = "",
    val favorites: Boolean = false,
    val stages: String = "",
    val category: String = "",
    val photoRecipe: String = ""
) {
    companion object {
        fun demoDataRecipe(): List<Recipe> {
            return List(10) { index ->

                Recipe(
                    id = index.toLong(),
                    describe = "$index Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    author = "$index Всемирная ассоциация любителей поесть",
                    favorites = false,
                    stages = "",
                    photoRecipe = "content://com.android.providers.media.documents/document/image%3A27",
                    category = "Russian"
                )


            }+List(10) { index ->

                Recipe(
                    id = 10+index.toLong(),
                    describe = "$index Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    author = "$index Всемирная ассоциация любителей поесть",
                    favorites = false,
                    stages = "",
                    photoRecipe = "content://com.android.providers.media.documents/document/image%3A27",
                    category = "Eastern"
                )


            }


        }
    }
}

