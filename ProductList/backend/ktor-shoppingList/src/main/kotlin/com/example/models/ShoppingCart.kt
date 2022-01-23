package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ShoppingCart(
    val customerId: String = "",
    val productId: Int = 0,
    val productName: String = "",
    val productDescription: String = "",
)

object ShoppingCartTable : Table(){
    val customerId = varchar("customerId", 100).references(CustomerTable.id)
    val productId = integer("productId").references(ProductTable.id)
    override val primaryKey = PrimaryKey(customerId, productId)

    val productName = varchar("productName", 100)
    val productDescription = varchar("productDescription", 2000)
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

fun getShoppingCartsByCustomerId(customerId : String) : List<ShoppingCart> {
    return transaction {
        ShoppingCartTable.select { ShoppingCartTable.customerId eq customerId }.map { it.toShoppingCart() }
    }
}

fun addShoppingCart(shoppingCart: ShoppingCart) {
    transaction {
        try{
            ShoppingCartTable.insert {
                it[customerId] = shoppingCart.customerId
                it[productId] = shoppingCart.productId
                it[productName] = shoppingCart.productName
                it[productDescription] = shoppingCart.productDescription
            }
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}

fun updateShoppingCart(cart: ShoppingCart) {
    transaction {
        try{
            ProductTable.update({ ShoppingCartTable.customerId eq cart.customerId }) {
                it[ShoppingCartTable.productName] = cart.productName
                it[ShoppingCartTable.productDescription] = cart.productDescription
            }
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}

fun deleteShoppingCartByCustomerId(customerId: String) {
    transaction {
        try{
            ShoppingCartTable.deleteWhere { ShoppingCartTable.customerId eq customerId }
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}

fun deleteShoppingCartByCustomerIdAndProductId(customerId: String, productId: Int) {
    transaction {
        try{
            ShoppingCartTable.deleteWhere { ShoppingCartTable.customerId eq customerId and (ShoppingCartTable.productId eq productId) }
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}

fun deleteAllShoppingCarts() {
    transaction {
        try{
            ShoppingCartTable.deleteAll()
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}

