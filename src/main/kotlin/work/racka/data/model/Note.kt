package work.racka.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String,
    val noteTitle: String,
    val description: String,
    val date: Long
)
