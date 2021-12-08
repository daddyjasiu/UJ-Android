package com.example.tables
import org.jetbrains.exposed.sql.Table

object ProductTable : Table("products") {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)
    val categoryId = integer("categoryId").references(CategoryTable.id)
    val name = varchar("name", 50)
    val quantity = integer("quantity")
    val price = double("price")
}