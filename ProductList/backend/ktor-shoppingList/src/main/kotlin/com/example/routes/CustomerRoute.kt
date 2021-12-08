package com.example.routes

import com.example.models.Customer
import com.example.models.toCustomer
import com.example.tables.CustomerTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.customerCRUD() {
    getCustomer()
    postCustomer()
    putCustomer()
    deleteCustomer()
}

private fun Application.getCustomer() {
    routing {
        get("/customer") {
            var customers = mutableListOf<Customer>()
            transaction {
                customers = CustomerTable.selectAll().map { it.toCustomer() }.toMutableList()
            }
            call.respond(customers)
        }

        get("/customer/{id}") {
            val id: Int = call.parameters["id"]!!.toInt()
            var customer = Customer()
            transaction {
                customer = CustomerTable.select { CustomerTable.id eq id }.map { it.toCustomer() }.first()
            }
            call.respond(customer)
        }
    }
}

private fun Application.postCustomer() {
    routing {
        post("/customer") {
            val customer = call.receive<Customer>()
            addCustomerToDatabase(customer)
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }
    }
}

private fun Application.putCustomer() {
    routing {
        put("/customer/{id}") {
            val id = call.parameters["id"]
            val customer = call.receive<Customer>()
            transaction {
                CustomerTable.update({ CustomerTable.id eq id!!.toInt() }) {
                    it[firstName] = customer.firstName
                    it[lastName] = customer.lastName
                    it[email] = customer.email
                }
            }
        }
    }
}

private fun Application.deleteCustomer() {
    routing {
        delete("/customer") {
            transaction {
                SchemaUtils.drop(CustomerTable)
            }
        }

        delete("/customer/{id}") {
            val id = call.parameters["id"]
            transaction {
                CustomerTable.deleteWhere { CustomerTable.id eq id!!.toInt() }
            }
        }
    }
}

private fun addCustomerToDatabase(customer: Customer) {
    transaction {
        CustomerTable.insert {
            it[id] = customer.id
            it[firstName] = customer.firstName
            it[lastName] = customer.lastName
            it[email] = customer.email
        }
    }
}

