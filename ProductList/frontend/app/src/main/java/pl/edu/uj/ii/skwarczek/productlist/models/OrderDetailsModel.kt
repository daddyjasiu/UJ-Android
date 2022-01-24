package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class OrderDetailsModel(
    var orderId : Int = 0,
    var productId : Int = 0,
    var productName : String = "",
    var productDescription : String = "",
)

open class OrderDetailsRealmModel(
    var orderId : Int = 0,
    var productId : Int = 0,
    var productName : String = "",
    var productDescription : String = "",
) : RealmObject()