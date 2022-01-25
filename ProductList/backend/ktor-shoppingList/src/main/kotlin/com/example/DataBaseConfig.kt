package com.example

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.models.*
import java.sql.Connection

fun dropExistingDB() {
    SchemaUtils.drop(ProductTable)
    SchemaUtils.drop(ShoppingCartTable)
    SchemaUtils.drop(OrderTable)
    SchemaUtils.drop(CustomerTable)
    SchemaUtils.drop(OrderDetailsTable)
}

fun createDB() {

    Database.connect("jdbc:sqlite:./db.sqlite", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {

        dropExistingDB()

        SchemaUtils.create(ProductTable)
        SchemaUtils.create(ShoppingCartTable)
        SchemaUtils.create(OrderTable)
        SchemaUtils.create(CustomerTable)
        SchemaUtils.create(OrderDetailsTable)

        createSampleDB()

    }
}

fun createSampleDB() {

}