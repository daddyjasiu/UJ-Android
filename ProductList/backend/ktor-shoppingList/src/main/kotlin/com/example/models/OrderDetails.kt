package com.example.models

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class OrderDetails(val orderId : Int, val productId : Int, val quantity : Int)

object OrderDetailsTable : Table() {
    val orderId = integer("orderId").references(OrderTable.id)
    val productId = integer("productId").references(ProductTable.id)
    val quantity = integer("quantity")
}

fun ResultRow.toOrderDetails() = OrderDetails(
    orderId = this[OrderDetailsTable.orderId],
    productId = this[OrderDetailsTable.productId],
    quantity = this[OrderDetailsTable.quantity]
)

fun getOrderDetailsByOrderId(orderId : Int) : List<OrderDetails> {
    return transaction {
        OrderDetailsTable.select { OrderDetailsTable.orderId eq orderId } . map {
            it.toOrderDetails()
        }
    }
}

fun getAllOrderDetails() : List<OrderDetails> {
    return transaction {
        OrderDetailsTable.selectAll().map { it.toOrderDetails() }
    }
}

fun insertOrderDetailsRow(orderId: Int, productId: Int, quantity: Int) {
    transaction {
        OrderDetailsTable.insert {
            it[OrderDetailsTable.orderId] = orderId
            it[OrderDetailsTable.productId] = productId
            it[OrderDetailsTable.quantity] = quantity
        }
    }
}

fun deleteOrderDetailsByOrderId(orderId : Int) {
    transaction {
        OrderDetailsTable.deleteWhere {
            OrderDetailsTable.orderId eq orderId
        }
    }
}