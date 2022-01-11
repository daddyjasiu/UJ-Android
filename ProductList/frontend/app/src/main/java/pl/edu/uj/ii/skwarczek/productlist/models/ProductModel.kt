package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class ProductModel (
    var id : Int,
    var name : String,
    var description : String,
    var price: Double
)

open class ProductRealmModel(
    var id : Int = 0,
    var name : String = "",
    var description : String = "",
    var price: Double = 0.0
) : RealmObject()