package pl.edu.uj.ii.skwarczek.productlist.utility

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import io.realm.Realm

import io.realm.RealmResults
import pl.edu.uj.ii.skwarczek.productlist.adapters.ProductListAdapter
import pl.edu.uj.ii.skwarczek.productlist.models.*
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

object RealmHelper {

    private var realm: Realm = Realm.getDefaultInstance()

    private var productListBackend: List<ProductModel> = listOf()
    private var shoppingCartListBackend: List<ShoppingCartModel> = listOf()
    private var orderListBackend: List<OrderModel> = listOf()
    private var orderDetailsListBackend: List<OrderDetailsModel> = listOf()

    fun clearDB(){
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

    fun getEverything(){
        val sth = Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .findAll()

        for (i in sth){
            println(i.id)
        }
    }

    fun addShoppingCart(cart: ShoppingCartRealmModel){
        realm.executeTransaction { bgRealm ->
            try{
                bgRealm.insert(cart)
            }
            catch(e: Exception){
                println(e.message)
            }
        }
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
            try {
                bgRealm.insert(product)
            }
            catch(e: Exception){
                print(e.message)
            }
        }
    }

    fun getProductById(id: Int): ProductRealmModel?{
        return Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .equalTo("id", id)
            .findFirst()
    }

    fun getAllProductsByCustomerId(customerId: String): List<ProductRealmModel> {

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

    fun placeOrder(order: OrderRealmModel){
        realm.executeTransaction { bgRealm ->
            try{
                bgRealm.insert(order)
            }
            catch(e: Exception){
                println(e.message)
            }
        }
    }

    fun placeOrderDetails(orderDetails: OrderDetailsRealmModel){
        realm.executeTransaction { bgRealm ->
            try{
                bgRealm.insert(orderDetails)
            }
            catch(e: Exception){
                println(e.message)
            }
        }
    }

    fun getOrdersByCustomerId(customerId: String) : List<OrderRealmModel>{
        return Realm.getDefaultInstance()
            .where(OrderRealmModel::class.java)
            .equalTo("customerId", customerId)
            .findAll()
    }

    fun syncDatabases(customerId: String){
        //Syncing Products
        var productListCache = getAllProductsByCustomerId(customerId)
        getAllProductsByCustomerIdFromBackend(customerId)
        val productListCacheCount = productListCache.count()
        val productListBackendCount = productListBackend.count()

        if(productListCacheCount != productListBackendCount){
            if(productListCacheCount < productListBackendCount){
                for(product in productListBackend){
                    val p = ProductRealmModel(product.id, product.customerId, product.name, product.description)
                    addProduct(p)
                }
            }
            else if(productListCacheCount > productListBackendCount){
                for(product in productListCache){
                    val p = ProductModel(product.id, product.customerId, product.name, product.description)
                    BackendHelper.addProductToBackend(p)
                }
            }
        }

        Globals.set(false)
    }

    private fun getAllProductsByCustomerIdFromBackend(customerId: String){
        val service = RetrofitService.create()
        val call = service.getProductsByCustomerIdCall(customerId)
        call.enqueue(object : Callback<List<ProductModel>> {
            override fun onResponse(call: Call<List<ProductModel>>, response: Response<List<ProductModel>>) {
                productListBackend = response.body()!!
            }
            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {

            }
        })
    }

}