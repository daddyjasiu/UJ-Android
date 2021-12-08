package com.example.models

import com.example.tables.OrderTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Order(
    val id: Int = 0,
    val shoppingCartId: Int = 0,
    val totalPrice: Double = 0.0
    )

fun ResultRow.toOrder() = Order(
    id = this[OrderTable.id],
    shoppingCartId = this[OrderTable.shoppingCartId],
    totalPrice = this[OrderTable.totalPrice]
)
