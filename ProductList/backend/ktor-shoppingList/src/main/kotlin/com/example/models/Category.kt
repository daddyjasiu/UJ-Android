package com.example.models

import com.example.tables.CategoryTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Category(
    val id: Int = 0,
    val name: String = ""
)

fun ResultRow.toCategory() = Category(
    id = this[CategoryTable.id],
    name = this[CategoryTable.name]
)