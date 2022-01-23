package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class OrderModel(
    var id: Int = 0,
    var customerId: String = "",
    var totalPrice: Double = -1.0
)

open class OrderRealmModel(
    @PrimaryKey
    var id: Int = 0,
    var customerId: String = "",
    var totalPrice: Double = -1.0
) : RealmObject()