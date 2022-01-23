package pl.edu.uj.ii.skwarczek.productlist.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class CustomerModel (
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = ""
)

open class CustomerRealmModel(
    @PrimaryKey
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = ""
) : RealmObject()