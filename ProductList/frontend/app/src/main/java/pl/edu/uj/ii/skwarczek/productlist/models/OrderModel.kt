package pl.edu.uj.ii.skwarczek.productlist.models

data class OrderModel(
    val id: Int,
    val customerId: Int,
    val totalPrice: Double,
)