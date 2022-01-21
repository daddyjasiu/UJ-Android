package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject

data class CustomerModel (
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = ""
)

open class CustomerRealmModel(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = ""
) : RealmObject()