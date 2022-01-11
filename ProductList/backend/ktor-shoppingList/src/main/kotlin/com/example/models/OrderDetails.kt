package com.example.models

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class OrderDetails(
    val orderId : Int,
    val productId : Int,
    )

object OrderDetailsTable : Table() {
    val orderId = integer("orderId").references(OrderTable.id)
    val productId = integer("productId").references(ProductTable.id)
}

fun ResultRow.toOrderDetails() = OrderDetails(
    orderId = this[OrderDetailsTable.orderId],
    productId = this[OrderDetailsTable.productId],
)

fun getAllOrderDetails() : List<OrderDetails> {
    return transaction {
        OrderDetailsTable.selectAll().map { it.toOrderDetails() }
    }
}

fun getOrderDetailsByOrderId(orderId : Int) : List<OrderDetails> {
    return transaction {
        OrderDetailsTable.select { OrderDetailsTable.orderId eq orderId } . map {
            it.toOrderDetails()
        }
    }
}

fun insertOrderDetailsRow(orderId: Int, productId: Int) {
    transaction {
        OrderDetailsTable.insert {
            it[OrderDetailsTable.orderId] = orderId
            it[OrderDetailsTable.productId] = productId
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