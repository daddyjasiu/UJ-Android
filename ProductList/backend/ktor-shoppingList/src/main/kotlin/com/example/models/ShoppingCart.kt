package com.example.models

import com.example.tables.ShoppingCartTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class ShoppingCart(
    val id: Int = 0,
    val productId: Int = 0,
    val amount: Int = 0,
    val totalPrice: Double = 0.0
)

fun ResultRow.toShoppingCart() = ShoppingCart(
    id = this[ShoppingCartTable.id],
    productId = this[ShoppingCartTable.productId],
    amount = this[ShoppingCartTable.amount],
    totalPrice = this[ShoppingCartTable.totalPrice]
)