package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Customer (
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    )

object CustomerTable : Table() {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)
    val firstName = varchar("firstName", 50)
    val lastName = varchar("lastName", 50)
    val email = varchar("email", 50)
    val password = varchar("password", 50)
}

fun ResultRow.toCustomer() = Customer(
    id = this[CustomerTable.id],
    firstName = this[CustomerTable.firstName],
    lastName = this[CustomerTable.lastName],
    email = this[CustomerTable.email],
    password = this[CustomerTable.password]
)

fun getAllCustomers() : List<Customer> {
    return transaction {
        CustomerTable.selectAll().map { it.toCustomer() }
    }
}

fun getCustomerById(id : Int) : List<Customer> {
    return transaction {
        CustomerTable.select { CustomerTable.id eq id}.map { it.toCustomer() }
    }
}

fun addCustomer(customer : Customer) {
    transaction {
        CustomerTable.insert {
            //it[id] = customer.id
            it[firstName] = customer.firstName
            it[lastName] = customer.lastName
            it[email] = customer.email
            it[password] = customer.password
        }
    }
}

fun updateCustomer(customer : Customer) {
    transaction {
        CustomerTable.update({ CustomerTable.id eq customer.id }) {
            //it[id] = customer.id
            it[firstName] = customer.firstName
            it[lastName] = customer.lastName
            it[email] = customer.email
            it[password] = customer.password
        }
    }
}

fun deleteAllCustomers() {
    transaction {
        CustomerTable.deleteAll()
    }
}

fun deleteCustomerById(id: Int) {
    transaction {
        CustomerTable.deleteWhere { CustomerTable.id eq id }
    }
}