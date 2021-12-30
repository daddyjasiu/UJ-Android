package pl.edu.uj.ii.skwarczek.productlist.models

data class ShoppingCartModel(
    val customerId: Int = 0,
    val productId: Int = 0,
    val quantity: Int = 0,
    val totalPrice: Double = 0.0
)