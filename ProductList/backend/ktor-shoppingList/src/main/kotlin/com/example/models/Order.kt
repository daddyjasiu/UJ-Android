package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Order(
    val id: Int = 0,
    val name: String = "",
    val customerId: String = "",
    val totalPrice: Double = -1.0,
    )

object OrderTable : Table() {
    val id = integer("id")
    override val primaryKey = PrimaryKey(id)

    val name = varchar("orderName", 200)
    val customerId = varchar("customerId", 100).references(CustomerTable.id)
    val totalPrice = double("totalPrice")
}

fun ResultRow.toOrder() = Order(
    id = this[OrderTable.id],
    name = this[OrderTable.name],
    customerId = this[OrderTable.customerId],
    totalPrice = this[OrderTable.totalPrice]
)

fun getAllOrders() : List<Order> {
    return transaction {
        OrderTable.selectAll().map { it.toOrder() }
    }
}

fun getOrdersByCustomerId(customerId: String) : List<Order> {
    return transaction {
        OrderTable.select { OrderTable.customerId eq customerId }.map { it.toOrder() }
    }
}

fun getOrderById(id : Int) : List<Order> {
    return transaction {
        OrderTable.select { OrderTable.id eq id }.map { it.toOrder() }
    }
}

fun placeOrder(id: Int, name: String, customerId: String, totalPrice: Double) {
    transaction {
        try {
            val customerCart = getShoppingCartsByCustomerId(customerId)

            val orderId: Int
            if (customerCart.isNotEmpty()) {
                orderId = OrderTable.insert {
                    it[OrderTable.id] = id
                    it[OrderTable.name] = name
                    it[OrderTable.customerId] = customerId
                    it[OrderTable.totalPrice] = totalPrice
                } get OrderTable.id

                for (cart in customerCart) {
                    insertOrderDetailsRow(orderId, cart.productId, cart.productName, cart.productDescription)
                }

                // after placing an order delete all products from cart
                deleteShoppingCartByCustomerId(customerId)

            } else {
                // TODO empty cart
            }
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}

fun updateOrder(order: Order){
    transaction {
        try{
            OrderTable.update({ ProductTable.id eq order.id }) {
                it[id] = order.id
                it[name] = order.name
                it[customerId] = order.customerId
                it[totalPrice] = order.totalPrice
            }
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}

fun deleteOrder(orderId : Int) {
    transaction {
        try {
            OrderTable.deleteWhere { OrderTable.id eq orderId }
            deleteOrderDetailsByOrderId(orderId)
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}


