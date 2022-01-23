package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class ShoppingCartModel(
    var customerId: String = "",
    var productId: Int = 0,
    var productName: String = "",
    var productDescription: String = "",
)

open class ShoppingCartRealmModel(
    var customerId: String = "",
    var productId: Int = 0,
    var productName: String = "",
    var productDescription: String = "",
) : RealmObject()