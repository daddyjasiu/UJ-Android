package com.example.models

import com.example.tables.ProductTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

val productsStorage = mutableListOf<Product>()

@Serializable
data class Product(
    val id: Int = 0,
    val categoryId: Int = 0,
    val name: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
)

fun ResultRow.toProduct() = Product(
    id = this[ProductTable.id],
    categoryId = this[ProductTable.categoryId],
    name = this[ProductTable.name],
    quantity = this[ProductTable.quantity],
    price = this[ProductTable.price]
)