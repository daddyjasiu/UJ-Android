package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class ProductModel (
    var id : Int,
    var name : String,
    var description : String,
)

open class ProductRealmModel(
    var id : Int = 0,
    var name : String = "",
    var description : String = "",
) : RealmObject()