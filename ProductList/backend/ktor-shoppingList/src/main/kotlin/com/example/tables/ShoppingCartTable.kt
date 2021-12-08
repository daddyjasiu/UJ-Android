package com.example.tables

import org.jetbrains.exposed.sql.Table

object ShoppingCartTable : Table("shoppingCarts"){
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)
    val productId = integer("productId").references(ProductTable.id)
    val amount = integer("amount")
    val totalPrice = double("totalPrice")
}