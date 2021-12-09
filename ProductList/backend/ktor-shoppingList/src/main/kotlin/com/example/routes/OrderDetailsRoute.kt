package com.example.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import com.example.models.*

fun Route.orderDetailsRouting() {
    route("/orderDetails") {

        get {
            call.respond(getAllOrderDetails())
        }

        get("/{order_id}") {
            val id = call.parameters["order_id"]
            if (id != null) {
                call.respond(getOrderDetailsByOrderId(id.toInt()))
            }
        }
    }
}