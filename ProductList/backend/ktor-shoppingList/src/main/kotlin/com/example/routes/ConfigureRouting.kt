package com.example.routes

import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        customerRouting()
        productRouting()
        shoppingCartRouting()
        orderRouting()
        orderDetailsRouting()
    }
}