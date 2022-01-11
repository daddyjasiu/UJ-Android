package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class ShoppingCartModel(
    var customerId: Int = 0,
    var productId: Int = 0,
    var quantity: Int = 0,
    var totalPrice: Double = 0.0
)

open class ShoppingCartRealmModel(
    var customerId: Int = 0,
    var productId: Int = 0,
    var quantity: Int = 0,
    var totalPrice: Double = 0.0
) : RealmObject()