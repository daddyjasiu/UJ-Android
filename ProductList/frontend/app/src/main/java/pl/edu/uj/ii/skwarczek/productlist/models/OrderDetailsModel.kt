package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class OrderDetailsModel(
    var orderId : Int,
    var productId : Int,
)

open class OrderDetailsRealmModel(
    var orderId : Int = 0,
    var productId : Int = 0,
) : RealmObject()