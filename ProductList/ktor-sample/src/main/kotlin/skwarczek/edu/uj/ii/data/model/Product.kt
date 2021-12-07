package skwarczek.edu.uj.ii.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val name: String,
    val description: String,
    val imageUrl: String
)