package com.example.tables
import org.jetbrains.exposed.sql.Table

object CustomerTable : Table("cusomers") {
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)
    val firstName = varchar("firstName", 50)
    val lastName = varchar("lastName", 50)
    val email = varchar("email", 50)
}