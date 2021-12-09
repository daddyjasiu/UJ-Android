package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Product(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0
)

object ProductTable : Table() {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", 50)
    val description = varchar("description", 500)
    val price = double("price")
}

fun ResultRow.toProduct() = Product(
    id = this[ProductTable.id],
    name = this[ProductTable.name],
    description = this[ProductTable.description],
    price = this[ProductTable.price]
)

fun getAllProducts() : List<Product> {
    return transaction {
        ProductTable.selectAll().map { it.toProduct() }
    }
}

fun getProduct(id : Int) : List<Product> {
    return transaction {
        ProductTable.select { ProductTable.id eq id }.map { it.toProduct() }
    }
}

fun addProduct(product: Product) {
    transaction {
        ProductTable.insert {
            //it[id] = product.id
            it[name] = product.name
            it[description] = product.description
            it[price] = product.price
        }
    }
}

fun updateProduct(product: Product) {
    transaction {
        ProductTable.update({ ProductTable.id eq product.id }) {
            it[name] = product.name
            it[description] = product.description
            it[price] = product.price
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