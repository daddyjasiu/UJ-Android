package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ShoppingCart(
    val customerId: Int = 0,
    val productId: Int = 0,
    val productName: String = "",
    val productDescription: String = "",
)

object ShoppingCartTable : Table(){
    val customerId = integer("customerId").references(CustomerTable.id)
    val productId = integer("productId").references(ProductTable.id)
    override val primaryKey = PrimaryKey(customerId, productId)

    val productName = varchar("productName", 50)
    val productDescription = varchar("productDescription", 500)
}

fun ResultRow.toShoppingCart() = ShoppingCart(
    customerId = this[ShoppingCartTable.customerId],
    productId = this[ShoppingCartTable.productId],
    productName = this[ShoppingCartTable.productName],
    productDescription = this[ShoppingCartTable.productDescription]

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

fun addShoppingCart(shoppingCart: ShoppingCart) {
    transaction {
        ShoppingCartTable.insert {
            it[customerId] = shoppingCart.customerId
            it[productId] = shoppingCart.productId
            it[productName] = shoppingCart.productName
            it[productDescription] = shoppingCart.productDescription
        }
    }
}

fun updateShoppingCart(cart: ShoppingCart) {
    transaction {
        ProductTable.update({ ShoppingCartTable.customerId eq cart.customerId }) {
            it[ShoppingCartTable.productName] = cart.productName
            it[ShoppingCartTable.productDescription] = cart.productDescription
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

