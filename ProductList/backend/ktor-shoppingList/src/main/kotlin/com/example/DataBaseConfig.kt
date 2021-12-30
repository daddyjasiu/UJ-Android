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
    val customer1 = Customer(1, "Karol", "Przystalski", "kprzystalski@gmail.com")
    val customer2 = Customer(2, "Jan", "Skwarczek", "janskw@gmail.com")
    val customer3 = Customer(3, "Smieszko", "Mieszko", "yoloswag@gmail.com")
    val product1 = Product(1, "p1", "product 1",100.0)
    val product2 = Product(2, "p2", "product 2",200.0)
    val product3 = Product(3, "p3", "product 3",300.0)
    val product4 = Product(4, "p4", "product 4",400.0)

    addProduct(product1)
    addProduct(product2)
    addProduct(product3)
    addProduct(product4)

    addCustomer(customer1)
    addCustomer(customer2)
    addCustomer(customer3)

    addToCart(1, 1)
    addToCart(1, 3)
    addToCart(1, 3)

    addToCart(2, 1)
    addToCart(2, 2)
    addToCart(2, 3)
    addToCart(2, 4)
    addToCart(2, 4)
    addToCart(2, 4)
    addToCart(2, 4)

    placeOrder(1)
    placeOrder(2)
}