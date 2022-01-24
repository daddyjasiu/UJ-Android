package com.example.models

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class OrderDetails(
    val orderId : Int,
    val productId : Int,
    val productName: String = "",
    val productDescription: String = "",
    )

object OrderDetailsTable : Table() {
    val orderId = integer("orderId").references(OrderTable.id)
    val productId = integer("productId").references(ProductTable.id)

    val productName = varchar("productName", 100)
    val productDescription = varchar("productDescription", 2000)
}

fun ResultRow.toOrderDetails() = OrderDetails(
    orderId = this[OrderDetailsTable.orderId],
    productId = this[OrderDetailsTable.productId],
    productName = this[OrderDetailsTable.productName],
    productDescription = this[OrderDetailsTable.productDescription]
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

fun insertOrderDetailsRow(orderId: Int, productId: Int, productName: String, productDescription: String) {
    transaction {
        try{
            OrderDetailsTable.insert {
                it[OrderDetailsTable.orderId] = orderId
                it[OrderDetailsTable.productId] = productId
                it[OrderDetailsTable.productName] = productName
                it[OrderDetailsTable.productDescription] = productName
            }
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}

fun deleteOrderDetailsByOrderId(orderId : Int) {
    transaction {
        try{
            OrderDetailsTable.deleteWhere {
                OrderDetailsTable.orderId eq orderId
            }
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}