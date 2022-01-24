package com.example.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import com.example.models.*
import io.ktor.request.*

fun Route.orderRouting() {
    route("/order") {
        // gets all orders
        get {
            call.respond(getAllOrders())
        }

        // gets all customer orders
        get("/customer/{customer_id}") {
            val id = call.parameters["customer_id"]
            if(id != null)
                call.respond(getOrdersByCustomerId(id.toString()))
        }

        // gets order by given id
        get("/{id}") {
            val id = call.parameters["id"]
            if(id != null)
                call.respond(getOrderById(id.toInt()))
        }

        put {
            val order = call.receive<Order>()
            call.respond(updateOrder(order))
        }

        post("/{order_id}/{order_name}/{customer_id}/{total_price}") {
            val orderId = call.parameters["order_id"]
            val orderName = call.parameters["order_name"]
            val customerId = call.parameters["customer_id"]
            val price = call.parameters["total_price"]
            if(orderId != null && orderName != null && customerId != null && price != null)
                call.respond(placeOrder(orderId.toInt(), orderName.toString(),  customerId.toString(), price.toDouble()))
        }

        // deletes an order by given id
        delete("/{id}") {
            val id = call.parameters["id"]
            if(id != null)
                call.respond(deleteOrder(id.toInt()))
        }

    }
}
