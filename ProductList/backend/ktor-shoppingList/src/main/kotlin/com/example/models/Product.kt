package com.example.models

import com.example.tables.ProductTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

val productsStorage = mutableListOf<Product>()

@Serializable
data class Product(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val quantity: Int,
    val price: Double
)

fun ResultRow.toProduct() = Product(
    id = this[ProductTable.id],
    categoryId = this[ProductTable.categoryId],
    name = this[ProductTable.name],
    quantity = this[ProductTable.quantity],
    price = this[ProductTable.price]
)