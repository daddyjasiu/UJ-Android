package com.example.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import com.example.models.*

fun Route.shoppingCartRouting() {
    route("/cart") {

        // gets all products from cart from every customer
        get {
            call.respond(getAllShoppingCarts())
        }

        // gets customer's cart
        get("/{customer_id}") {
            val customerId = call.parameters["customer_id"]
            if(customerId != null) {
                call.respond(getShoppingCartByCustomerId(customerId.toInt()))
            }
        }

        //creates shopping cart
        post {
            val cart = call.receive<ShoppingCart>()
            call.respond(addShoppingCart(cart))
        }

        put {
            val cart = call.receive<ShoppingCart>()
            call.respond(updateShoppingCart(cart))
        }

        // deteles all products from cart from all customers
        delete {
            call.respond(deleteAllShoppingCarts())
        }

        // deletes a product from cart
        delete("/{customer_id}/{product_id}") {
            val customerId = call.parameters["customer_id"]
            val productId = call.parameters["product_id"]
            if(customerId != null && productId != null) {
                call.respond(deleteShoppingCartByCustomerIdAndProductId(customerId.toInt(), productId.toInt()))
            }
        }

        // delete customer cart
        delete("/{customer_id}") {
            val customerId = call.parameters["customer_id"]
            if(customerId != null) {
                call.respond(deleteShoppingCartByCustomerId(customerId.toInt()))
            }
        }
    }
}