package com.example.models

import com.example.tables.CustomerTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Customer (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    )

fun ResultRow.toCustomer() = Customer(
    id = this[CustomerTable.id],
    firstName = this[CustomerTable.firstName],
    lastName = this[CustomerTable.lastName],
    email = this[CustomerTable.email]
)
