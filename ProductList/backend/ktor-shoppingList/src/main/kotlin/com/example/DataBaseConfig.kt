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
//    val customer1 = Customer(1, "Karol", "Przystalski", "kprzystalski@gmail.com", "pass1")
//    val customer2 = Customer(2, "Jan", "Skwarczek", "janskw@gmail.com", "pass2")
//    val customer3 = Customer(3, "Smieszko", "Mieszko", "yoloswag@gmail.com", "pass3")
//    val customer4 = Customer(4, "Tescik", "Tescikowski", "prankyboi@gmail.com", "pass4")
//
//    val product1 = Product(1, customer1.id, "p1", "product 1")
//    val product2 = Product(2, customer2.id, "p2", "product 2")
//    val product3 = Product(3, customer3.id, "p3", "product 3")
//    val product4 = Product(4, customer4.id, "p4", "product 4")
//
//    val shoppingCart1 = ShoppingCart(customer1.id, product1.id, product1.name, product1.description)
//    val shoppingCart2 = ShoppingCart(customer1.id, product2.id, product2.name, product2.description)
//    val shoppingCart3 = ShoppingCart(customer3.id, product1.id, product1.name, product1.description)
//    val shoppingCart4 = ShoppingCart(customer3.id, product3.id, product3.name, product3.description)
//    val shoppingCart5 = ShoppingCart(customer3.id, product4.id, product4.name, product4.description)
//
//    addCustomer(customer1)
//    addCustomer(customer2)
//    addCustomer(customer3)
//    addCustomer(customer4)
//
//    addProduct(product1)
//    addProduct(product2)
//    addProduct(product3)
//    addProduct(product4)
//
//    addShoppingCart(shoppingCart1)
//    addShoppingCart(shoppingCart2)
//    addShoppingCart(shoppingCart3)
//    addShoppingCart(shoppingCart4)
//    addShoppingCart(shoppingCart5)
//
////   placeOrder(1, 123.0)
////   placeOrder(2)
}