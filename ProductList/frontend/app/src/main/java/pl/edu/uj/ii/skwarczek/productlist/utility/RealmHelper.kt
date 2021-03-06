package pl.edu.uj.ii.skwarczek.productlist.utility

import io.realm.Realm

import io.realm.RealmResults
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
                println(product.name + "inserted!")
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

    fun getOrderDetailsByOrderId(orderId: Int) : List<OrderDetailsRealmModel>{
        return Realm.getDefaultInstance()
            .where(OrderDetailsRealmModel::class.java)
            .equalTo("orderId", orderId)
            .findAll()
    }

    fun syncDatabases(customerId: String){
        //Syncing Products
        val productListCache = getAllProductsByCustomerId(customerId)
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

        //Syncing ShoppingCarts
        val shoppingCartListCache = getShoppingCartsByCustomerId(customerId)
        getAllShoppingCartsByCustomerIdFromBackend(customerId)
        val shoppingCartListCacheCount = shoppingCartListCache.count()
        val shoppingCartListBackendCount = shoppingCartListBackend.count()

        if(shoppingCartListCacheCount != shoppingCartListBackendCount){
            if(shoppingCartListCacheCount < shoppingCartListBackendCount){
                for(cart in shoppingCartListBackend){
                    val c = ShoppingCartRealmModel(cart.customerId, cart.productId, cart.productName, cart.productDescription)
                    addShoppingCart(c)
                }
            }
            else if(shoppingCartListCacheCount > shoppingCartListBackendCount){
                for(cart in shoppingCartListCache){
                    val c = ShoppingCartModel(cart.customerId, cart.productId, cart.productName, cart.productDescription)
                    BackendHelper.addShoppingCartToBackend(c)
                }
            }
        }

        //Syncing Orders
        val orderListCache = getOrdersByCustomerId(customerId)
        getAllOrdersByCustomerIdFromBackend(customerId)
        val orderListCacheCount = orderListCache.count()
        val orderListBackendCount = orderListBackend.count()

        if(orderListCacheCount != orderListBackendCount){
            if(orderListCacheCount < orderListBackendCount){
                for(order in orderListBackend){
                    val o = OrderRealmModel(order.id, order.name, order.customerId, order.totalPrice)
                    placeOrder(o)
                }
            }
            else if(orderListCacheCount > orderListBackendCount){
                for(order in orderListCache){
                    val o = OrderModel(order.id, order.name, order.customerId, order.totalPrice)
                    BackendHelper.placeOrderToBackend(o.id, o.name, o.customerId, o.totalPrice)
                }
            }
        }

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

    private fun getAllShoppingCartsByCustomerIdFromBackend(customerId: String){
        val service = RetrofitService.create()
        val call = service.getShoppingCartsByCustomerIdCall(customerId)
        call.enqueue(object : Callback<List<ShoppingCartModel>> {
            override fun onResponse(call: Call<List<ShoppingCartModel>>, response: Response<List<ShoppingCartModel>>) {
                shoppingCartListBackend = response.body()!!
            }
            override fun onFailure(call: Call<List<ShoppingCartModel>>, t: Throwable) {

            }
        })
    }

    private fun getAllOrdersByCustomerIdFromBackend(customerId: String){
        val service = RetrofitService.create()
        val call = service.getOrdersByCustomerIdCall(customerId)
        call.enqueue(object : Callback<List<OrderModel>> {
            override fun onResponse(call: Call<List<OrderModel>>, response: Response<List<OrderModel>>) {
                orderListBackend = response.body()!!
            }
            override fun onFailure(call: Call<List<OrderModel>>, t: Throwable) {

            }
        })
    }

}