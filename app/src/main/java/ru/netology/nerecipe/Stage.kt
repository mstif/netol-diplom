package ru.netology.nerecipe
import kotlinx.serialization.Serializable

@Serializable
data class Stage(val id:Long = 0,
                 val content: String = "",
                 val position: Int = 1,
                 val photo: String = ""){
    companion object {
        fun demoDataStages(): List<Stage> {
            return List(10) { index ->

                Stage(
                    id = index.toLong(),
                    content = "мелко нашинковать три мухомора, тщательно контролируя уровень воды в долине Нила",
                    photo =    "content://com.android.providers.media.documents/document/image%3A27",
                    position = index

                )




            }


        }
    }
}