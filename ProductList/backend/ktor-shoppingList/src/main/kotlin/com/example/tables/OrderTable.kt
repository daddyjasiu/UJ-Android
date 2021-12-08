package com.example.tables
import org.jetbrains.exposed.sql.Table

object OrderTable : Table("orders") {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)
    val shoppingCartId = integer("shoppingCartId").references(ShoppingCartTable.id)
    val totalPrice = double("totalPrice").references(ShoppingCartTable.totalPrice)
}