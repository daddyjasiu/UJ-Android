package com.example.routes
import com.example.models.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.productRouting() {
    route("/product") {
        // gets all products
        get {
            call.respond(getAllProducts())
        }

        // gets product by given id
        get("/{customerId}") {
            val customerId = call.parameters["customerId"]
            if(customerId != null) {
                val product = getProductByCustomerId(customerId.toString())
                call.respond(product)
            }
        }

        // update product
        put {
            val product = call.receive<Product>()
            call.respond(updateProduct(product))
        }

        // adds product
        post {
            val product = call.receive<Product>()
            call.respond(addProduct(product))
        }

        // deletes all products
        delete {
            call.respond(deleteAllProducts())
        }

        // deletes product
        delete("/{customerId}/{productId}") {
            val customerId = call.parameters["customerId"]
            val productId = call.parameters["productId"]
            if(customerId != null && productId != null)
                call.respond(deleteProductByProductIdAndCustomerId(customerId.toString(), productId.toInt()))
        }
    }
}