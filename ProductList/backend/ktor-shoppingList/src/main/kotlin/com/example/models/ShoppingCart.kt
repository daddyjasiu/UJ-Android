package com.example.models

import com.example.tables.ShoppingCartTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class ShoppingCart(
    val id: Int,
    val productId: Int,
    val amount: Int,
    val totalPrice: Double
)

fun ResultRow.toShoppingCart() = ShoppingCart(
    id = this[ShoppingCartTable.id],
    productId = this[ShoppingCartTable.productId],
    amount = this[ShoppingCartTable.amount],
    totalPrice = this[ShoppingCartTable.totalPrice]
)