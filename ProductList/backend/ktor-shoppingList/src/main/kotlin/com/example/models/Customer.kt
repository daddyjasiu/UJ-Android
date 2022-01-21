package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import javax.management.Query.and

@Serializable
data class Customer (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    )

object CustomerTable : Table() {
    val id = varchar("id", 100)
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

fun getCustomerById(id : String) : Customer? {
    return transaction {
        CustomerTable.select { CustomerTable.id eq id}.map { it.toCustomer() }.singleOrNull()
    }
}

fun getCustomerByEmailAndPassword(email: String, password: String) : Customer?{
    return transaction {
        CustomerTable.select {(CustomerTable.email eq email) and (CustomerTable.password eq password)}.map { it.toCustomer() }.singleOrNull()
    }
}

fun addCustomer(customer : Customer) {
    transaction {
        CustomerTable.insert {
            it[id] = customer.id
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
            it[id] = customer.id
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

fun deleteCustomerById(id: String) {
    transaction {
        CustomerTable.deleteWhere { CustomerTable.id eq id }
    }
}