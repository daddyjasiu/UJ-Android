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
                val product = getProductByCustomerId(customerId.toInt())
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
        delete("/{id}") {
            val id = call.parameters["id"]
            if(id != null)
                call.respond(deleteProductById(id.toInt()))
        }
    }
}