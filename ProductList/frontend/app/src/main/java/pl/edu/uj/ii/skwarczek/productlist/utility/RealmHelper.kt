package pl.edu.uj.ii.skwarczek.productlist.utility

import io.realm.Realm
import pl.edu.uj.ii.skwarczek.productlist.models.CustomerRealmModel
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel
import kotlin.random.Random

import io.realm.RealmResults
import pl.edu.uj.ii.skwarczek.productlist.models.ShoppingCartRealmModel

object RealmHelper {

    private var realm: Realm = Realm.getDefaultInstance()

    fun clearDB(){
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

    fun addProductToCart(currentUser: CustomerRealmModel, product: ProductRealmModel){

        val cart = ShoppingCartRealmModel(currentUser.id, product.id, )
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            bgRealm.insert(cart)

        }, Realm.Transaction.OnSuccess {
            println("Cart added to local Realm database with credentials:")
            println("PRODUCT_ID: ${cart.productId}, CUSTOMER_ID: ${cart.customerId}")
        })
    }

    fun getAllShoppingCarts(): List<ShoppingCartRealmModel>{

        return Realm.getDefaultInstance()
            .where(ShoppingCartRealmModel::class.java)
            .findAll()
    }

    fun deleteAllShoppingCartsByUserId(id: Int){
        realm.executeTransaction{
            val rows: RealmResults<ShoppingCartRealmModel> =
                realm.where(ShoppingCartRealmModel::class.java).equalTo("customerId", id).findAll()
            rows.deleteAllFromRealm()
        }
    }

    fun addProduct(product: ProductRealmModel){
        realm.executeTransaction(Realm.Transaction { bgRealm ->
            bgRealm.insert(product)
        })
    }

    fun getProductById(id: Int): ProductRealmModel?{
        return Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .equalTo("id", id)
            .findFirst()
    }

    fun getAllProducts(): MutableList<ProductRealmModel> {

        return Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .findAll()
    }

    fun deleteProductById(id: Int){
        realm.executeTransaction{
            val rows: RealmResults<ProductRealmModel> =
                realm.where(ProductRealmModel::class.java).equalTo("id", id).findAll()
            rows.deleteAllFromRealm()
        }

    }

    fun addCustomer(customer: CustomerRealmModel){
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