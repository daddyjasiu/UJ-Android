package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ShoppingCart(
    val customerId: Int = 0,
    val productId: Int = 0,
)

object ShoppingCartTable : Table(){
    val customerId = integer("customerId").references(CustomerTable.id)
    val productId = integer("productId").references(ProductTable.id)
    override val primaryKey = PrimaryKey(customerId, productId)
}

fun ResultRow.toShoppingCart() = ShoppingCart(
    customerId = this[ShoppingCartTable.customerId],
    productId = this[ShoppingCartTable.productId],
)

fun getAllShoppingCarts() : List<ShoppingCart> {
    return transaction {
        ShoppingCartTable.selectAll().map { it.toShoppingCart() }
    }
}

fun getShoppingCartByCustomerId(customerId : Int) : List<ShoppingCart> {
    return transaction {
        ShoppingCartTable.select { ShoppingCartTable.customerId eq customerId }.map { it.toShoppingCart() }
    }
}

fun addToCart(customerId: Int, productId: Int) {
    transaction {
        ShoppingCartTable.insert {
            it[ShoppingCartTable.customerId] = customerId
            it[ShoppingCartTable.productId] = productId
        }
    }
}

fun deleteShoppingCartByCustomerId(customerId: Int) {
    transaction {
        ShoppingCartTable.deleteWhere { ShoppingCartTable.customerId eq customerId }
    }
}

fun deleteShoppingCartByCustomerIdAndProductId(customerId: Int, productId: Int) {
    transaction {
        ShoppingCartTable.deleteWhere { ShoppingCartTable.customerId eq customerId and (ShoppingCartTable.productId eq productId) }
    }
}

fun deleteAllShoppingCarts() {
    transaction {
        ShoppingCartTable.deleteAll()
    }
}

