package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class OrderModel(
    var id: Int = 0,
    var customerId: String = "",
    var totalPrice: Double = -1.0
)

open class OrderRealmModel(
    var id: Int = 0,
    var customerId: String = "",
    var totalPrice: Double = -1.0
) : RealmObject()