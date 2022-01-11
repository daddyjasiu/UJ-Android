package com.example.routes

import io.ktor.application.*
import io.ktor.http.*
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
                call.respond(getOrdersByCustomerId(id.toInt()))
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

        // adds new order
        post("/{customer_id}/{total_price}") {
            val id = call.parameters["customer_id"]
            val price = call.parameters["total_price"]
            if(id != null && price != null)
                call.respond(placeOrder(id.toInt(), price.toDouble()))
        }

        // deletes an order by given id
        delete("/{id}") {
            val id = call.parameters["id"]
            if(id != null)
                call.respond(deleteOrder(id.toInt()))
        }

    }
}
