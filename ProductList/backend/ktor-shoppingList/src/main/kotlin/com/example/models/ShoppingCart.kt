package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ShoppingCart(
    val customerId: Int = 0,
    val productId: Int = 0,
    val quantity: Int = 0,
    val totalPrice: Double = 0.0
)

object ShoppingCartTable : Table(){
    val customerId = integer("customerId").references(CustomerTable.id)
    val productId = integer("productId").references(ProductTable.id)
    override val primaryKey = PrimaryKey(customerId, productId)

    val quantity = integer("quantity")
    val totalPrice = double("totalPrice")
}

fun ResultRow.toShoppingCart() = ShoppingCart(
    customerId = this[ShoppingCartTable.customerId],
    productId = this[ShoppingCartTable.productId],
    quantity = this[ShoppingCartTable.quantity],
    totalPrice = this[ShoppingCartTable.totalPrice]
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
        val productPrice = getProduct(productId)[0].price
        val count = ShoppingCartTable.select { ShoppingCartTable.customerId eq customerId and (ShoppingCartTable.productId eq productId) }.count()
        // product already in cart, increase quantity
        if(count > 0) {
            ShoppingCartTable.update({ (ShoppingCartTable.customerId eq customerId) and (ShoppingCartTable.productId eq productId) }) {
                with(SqlExpressionBuilder) {
                    it[quantity] = quantity + 1
                    it[totalPrice] = totalPrice + productPrice
                }
            }
        } else {
            ShoppingCartTable.insert {
                it[ShoppingCartTable.customerId] = customerId
                it[ShoppingCartTable.productId] = productId
                it[quantity] = 1
                it[totalPrice] = productPrice
            }
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

