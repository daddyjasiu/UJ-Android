package pl.edu.uj.ii.skwarczek.productlist.utility

import io.realm.Realm

import io.realm.RealmResults
import pl.edu.uj.ii.skwarczek.productlist.models.*
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RealmHelper {

    private var realm: Realm = Realm.getDefaultInstance()

    fun clearDB(){
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

    fun addShoppingCart(cart: ShoppingCartRealmModel){

        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            bgRealm.insert(cart)

        }, Realm.Transaction.OnSuccess {
            println("Cart added to local Realm database with credentials:")
            println("PRODUCT_ID: ${cart.productId}, CUSTOMER_ID: ${cart.customerId}")
        })
    }

    fun getShoppingCartsByCustomerId(customerId: String): List<ShoppingCartRealmModel>{

        return Realm.getDefaultInstance()
            .where(ShoppingCartRealmModel::class.java)
            .equalTo("customerId", customerId)
            .findAll()
    }

    fun deleteAllShoppingCartsByCustomerId(customerId: String){
        realm.executeTransaction{
            val rows: RealmResults<ShoppingCartRealmModel> =
                realm.where(ShoppingCartRealmModel::class.java)
                    .equalTo("customerId", customerId)
                    .findAll()
            rows.deleteAllFromRealm()
        }
    }

    fun deleteShoppingCartByCustomerIdAndProductId(customerId: String, productId: Int){
        realm.executeTransaction{
            val rows: RealmResults<ShoppingCartRealmModel> =
                realm.where(ShoppingCartRealmModel::class.java)
                    .equalTo("customerId", customerId)
                    .and()
                    .equalTo("productId", productId)
                    .findAll()
            rows.deleteAllFromRealm()
        }
    }

    fun addProduct(product: ProductRealmModel){
        realm.executeTransaction { bgRealm ->
            bgRealm.insert(product)
        }
    }

    fun getProductById(id: Int): ProductRealmModel?{
        return Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .equalTo("id", id)
            .findFirst()
    }

    fun getAllProductsByCustomerId(customerId: String): MutableList<ProductRealmModel> {

        return Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .equalTo("customerId", customerId)
            .findAll()
    }

    fun deleteProductByProductIdAndCustomerId(productId: Int, customerId: String){
        realm.executeTransaction{
            val rows: RealmResults<ProductRealmModel> =
                realm.where(ProductRealmModel::class.java)
                    .equalTo("id", productId)
                    .and()
                    .equalTo("customerId", customerId)
                    .findAll()
            rows.deleteAllFromRealm()
        }

    }

    fun placeOrder(order: OrderRealmModel, orderDetails: OrderDetailsRealmModel){
        realm.executeTransaction { bgRealm ->
            bgRealm.insert(order)
            bgRealm.insert(orderDetails)
        }
    }

}