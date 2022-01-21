package com.example.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import com.example.models.*
import io.ktor.http.*

fun Route.customerRouting() {

    route("/customer") {

        // get all customers
        get {
            call.respond(getAllCustomers())
        }

        // gets customer by given id
        get("/{id}") {
            val id = call.parameters["id"]
            if(id != null) {
                val customer = getCustomerById(id.toString())
                if(customer != null)
                    call.respond(customer)
                else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }

        get("/{email}/{password}"){
            val email = call.parameters["email"]
            val password = call.parameters["password"]

            if(email != null && password != null) {
                val customer = getCustomerByEmailAndPassword(email, password)
                if(customer != null)
                    call.respond(customer)
                else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }

        // update customer
        put {
            val customer = call.receive<Customer>()
            call.respond(updateCustomer(customer))
        }

        // adds customer
        post {
            val customer = call.receive<Customer>()
            call.respond(addCustomer(customer))
        }

        // deletes all customers
        delete {
            call.respond(deleteAllCustomers())
        }

        // deletes customer by given id
        delete("/{id}") {
            val id = call.parameters["id"]
            if(id != null)
                call.respond(deleteCustomerById(id.toString()))
        }
    }
}