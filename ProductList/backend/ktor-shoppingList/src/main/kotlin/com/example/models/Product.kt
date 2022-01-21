package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Product(
    val id: Int = 0,
    val customerId: String = "",
    val name: String = "",
    val description: String = "",
)

object ProductTable : Table() {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val customerId = varchar("customerId", 100)
    val name = varchar("name", 50)
    val description = varchar("description", 500)
}

fun ResultRow.toProduct() = Product(
    id = this[ProductTable.id],
    customerId = this[ProductTable.customerId],
    name = this[ProductTable.name],
    description = this[ProductTable.description],
)

fun getAllProducts() : List<Product> {
    return transaction {
        ProductTable.selectAll().map { it.toProduct() }
    }
}

fun getProductByCustomerId(customerId : String) : List<Product> {
    return transaction {
        ProductTable.select { ProductTable.customerId eq customerId }.map { it.toProduct() }
    }
}

fun addProduct(product: Product) {
    transaction {
        ProductTable.insert {
            //it[id] = product.id
            it[customerId] = product.customerId
            it[name] = product.name
            it[description] = product.description
        }
    }
}

fun updateProduct(product: Product) {
    transaction {
        ProductTable.update({ ProductTable.id eq product.id }) {
            it[customerId] = product.customerId
            it[name] = product.name
            it[description] = product.description
        }
    }
}

fun deleteAllProducts() {
    transaction {
        ProductTable.deleteAll()
    }
}

fun deleteProductById(id: Int) {
    transaction {
        ProductTable.deleteWhere { ProductTable.id eq id }
    }
}