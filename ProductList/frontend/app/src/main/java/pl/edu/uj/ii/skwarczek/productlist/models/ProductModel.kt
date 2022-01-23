package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class ProductModel (
    var id: Int = 0,
    var customerId: String = "",
    var name: String = "",
    var description: String = "",
)

open class ProductRealmModel(
    @PrimaryKey
    var id: Int = 0,
    var customerId: String = "",
    var name: String = "",
    var description: String = "",
) : RealmObject()