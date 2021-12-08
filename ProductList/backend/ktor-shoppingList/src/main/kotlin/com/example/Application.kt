package com.example

import io.ktor.application.*
import com.example.plugins.*
import com.example.routes.customerCRUD
import com.example.tables.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    Database.connect("jdbc:sqlite:database.sqlite", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction{
        SchemaUtils.create(CustomerTable)
        SchemaUtils.create(CategoryTable)
        SchemaUtils.create(ProductTable)
        SchemaUtils.create(ShoppingCartTable)
        SchemaUtils.create(OrderTable)
    }

    customerCRUD()
    //----------------------------------
    configureRouting()
    configureMonitoring()
    configureSerialization()
}
