package pl.edu.uj.ii.skwarczek.productlist.data

import android.util.Log
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import pl.edu.uj.ii.skwarczek.productlist.services.ShoppingCartService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class ShoppingCartRealm : RealmObject() {
    var customerId: Int = -1

    @PrimaryKey
    var productId: Int = -1
    var quantity: Int = -1
    var totalPrice: Double = -1.0
}

class ShoppingCart {
    var customerId: Int = -1
    var productId: Int = -1
    var quantity: Int = -1
    var totalPrice: Double = -1.0
}

fun isCartEmpty(): Boolean {

    val count = Realm.getDefaultInstance().where(ShoppingCartRealm::class.java)
        .equalTo("customerId", CURRENT_CUSTOMER_ID)
        .count()

    return count < 1
}

fun cartToString(): String {
    var result = ""

    Realm.getDefaultInstance()
        .where(ShoppingCartRealm::class.java)
        .equalTo("customerId", CURRENT_CUSTOMER_ID)
        .findAll().forEach {
            result += "Product: " + Products.productDetails(it.productId) +
                    ", quantity: " + it.quantity +
                    ", total price: " + it.totalPrice +
                    "\n\n"
        }

    return result
}

fun cartTotalPrice(): Int {
    var result = 0

    Realm.getDefaultInstance()
        .where(ShoppingCartRealm::class.java)
        .equalTo("customerId", CURRENT_CUSTOMER_ID)
        .findAll().forEach {
            result += ((Realm.getDefaultInstance().where(ProductRealm::class.java)
                .equalTo("id", it.productId)
                .findFirst()?.price ?: 0) * it.quantity)
        }

    return result
}

fun removeCartItem(productId: Int) {
    val service = ShoppingCartService.create()
    val call = service.deleteCartItemCall(CURRENT_CUSTOMER_ID, productId)
    call.enqueue(object : Callback<ShoppingCart> {
        override fun onResponse(call: Call<ShoppingCart>, response: Response<ShoppingCart>) {
            Log.d("ITEM DELETE SUCCESS", response.message())
        }

        override fun onFailure(call: Call<ShoppingCart>, t: Throwable) {
            Log.d("ITEM DELETE FAIL", t.message.toString())
        }

    })

    Realm.getDefaultInstance().beginTransaction()

    Realm.getDefaultInstance().where(ShoppingCartRealm::class.java)
        .equalTo("customerId", CURRENT_CUSTOMER_ID)
        .equalTo("productId", productId)
        .findAll().forEach {
            it.quantity -= 1
        }

    Realm.getDefaultInstance().where(ShoppingCartRealm::class.java)
        .equalTo("customerId", CURRENT_CUSTOMER_ID)
        .lessThan("quantity", 1)
        .findAll()
        .deleteAllFromRealm()


    Realm.getDefaultInstance().commitTransaction()
}

fun postCart(productId: Int) {
    val service = ShoppingCartService.create()
    val call = service.postCartItemCall(CURRENT_CUSTOMER_ID, productId)
    call.enqueue(object : Callback<ShoppingCart> {
        override fun onResponse(call: Call<ShoppingCart>, response: Response<ShoppingCart>) {
            Log.d("CART ITEM POSTED SUCCESSFULLY", response.message().toString())
        }

        override fun onFailure(call: Call<ShoppingCart>, t: Throwable) {
            Log.d("CART ITEM POST FAILED", t.message.toString())
        }

    })

    //getCartIntoDB

    // refresh cart so it is up to date
    refreshCart()
}

fun refreshCart() {
    Realm.getDefaultInstance().beginTransaction()
    Realm.getDefaultInstance().where(ShoppingCartRealm::class.java)
        .equalTo("customerId", CURRENT_CUSTOMER_ID)
        .findAll().deleteAllFromRealm()
    Realm.getDefaultInstance().commitTransaction()
    getCartIntoDB()
}

fun getCartIntoDB() {
    val service = ShoppingCartService.create()
    val call = service.getCartByIdCall(CURRENT_CUSTOMER_ID)
    call.enqueue(object : Callback<List<ShoppingCart>> {
        override fun onResponse(
            call: Call<List<ShoppingCart>>,
            response: Response<List<ShoppingCart>>
        ) {
            if (response.code() == 200) {
                val cartResponse = response.body()!!

                for (item in cartResponse) {
                    val tmpItem = ShoppingCartRealm().apply {
                        this.customerId = item.customerId
                        this.productId = item.productId
                        this.quantity = item.quantity
                    }

                    Realm.getDefaultInstance().executeTransactionAsync {
                        it.copyToRealmOrUpdate(tmpItem)
                    }
                }

                Log.d("GET_CART_FROM_DB", "Cart get successful")
            }

        }

        override fun onFailure(call: Call<List<ShoppingCart>>, t: Throwable) {
            Log.d("GET_CART_FROM_DB", t.message.toString())
        }

    })
}