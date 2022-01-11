package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class CustomerModel (
    var id: Int,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String
)

open class CustomerRealmModel(
    var id: Int = 0,
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = ""
) : RealmObject()