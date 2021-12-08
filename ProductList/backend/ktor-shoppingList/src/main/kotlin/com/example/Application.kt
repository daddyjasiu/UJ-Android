package com.example

import io.ktor.application.*
import com.example.plugins.*
import com.example.routes.registerCustomerRoutes
import com.example.routes.registerOrderRoutes
import com.example.routes.registerProductRoutes
import com.example.tables.CustomerTable
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
    }

    registerCustomerRoutes()
    registerOrderRoutes()
    registerProductRoutes()
    //----------------------------------
    configureRouting()
    configureMonitoring()
    configureSerialization()
}
