package pl.edu.uj.ii.skwarczek.productlist.activities

import io.realm.Realm
import pl.edu.uj.ii.skwarczek.productlist.models.CustomerRealmModel
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel
import kotlin.random.Random

import io.realm.RealmResults




object RealmHelper {

    private var realm: Realm = Realm.getDefaultInstance()

    fun clearDB(){
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

    fun addProductToDB(product: ProductRealmModel){
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            bgRealm.insert(product)

        }, Realm.Transaction.OnSuccess {
            println("Product added to local Realm database with credentials:")
            println("ID: ${product.id}, NAME: ${product.name}, DESCRIPTION: ${product.description}")
        })
    }

    fun getAllProducts(): List<ProductRealmModel>{
        val products = Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .findAll()

        return products
    }

    fun deleteProductById(id: Int){
        realm.executeTransaction{
            val rows: RealmResults<ProductRealmModel> =
                realm.where(ProductRealmModel::class.java).equalTo("id", id).findAll()
            rows.deleteAllFromRealm()
        }

    }

    fun addCustomerToDB(customer: CustomerRealmModel){
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            val random = Random.nextInt(0, Int.MAX_VALUE)
            bgRealm.insert(
                CustomerRealmModel(
                random,
                customer.firstName,
                customer.lastName,
                customer.email,
                customer.password
            ))

        }, Realm.Transaction.OnSuccess {
            println("Customer added to local Realm database with credentials:")
            println("ID: ${customer.id}, E-MAIL: ${customer.email}, PASSWORD: ${customer.password}")
        })
    }

    fun getCustomerById(id: Int): CustomerRealmModel? {

        return Realm.getDefaultInstance()
            .where(CustomerRealmModel::class.java)
            .equalTo("id", id)
            .findFirst()
    }

    fun getCustomerByEmailAndPassword(email: String, password: String): CustomerRealmModel? {

        return Realm.getDefaultInstance()
            .where(CustomerRealmModel::class.java)
            .equalTo("email", email)
            .equalTo("password", password)
            .findFirst()
    }

    fun getLatestCustomer(): CustomerRealmModel? {

        return Realm.getDefaultInstance()
            .where(CustomerRealmModel::class.java)
            .findAll()
            .last(null)
    }

    fun checkIfCustomerExistsById(id: Int): Boolean{

        val customer = Realm.getDefaultInstance()
            .where(CustomerRealmModel::class.java)
            .equalTo("id", id)
            .findFirst()

        return customer == null
    }

    fun syncRealmWithSQLite(){

    }

}