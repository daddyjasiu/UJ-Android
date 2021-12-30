package pl.edu.uj.ii.skwarczek.productlist.models

data class OrderDetailsModel(
    val orderId : Int,
    val productId : Int,
    val quantity : Int
)