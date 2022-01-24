package pl.edu.uj.ii.skwarczek.productlist.utility

import android.util.Log
import pl.edu.uj.ii.skwarczek.productlist.models.OrderModel
import pl.edu.uj.ii.skwarczek.productlist.models.ProductModel
import pl.edu.uj.ii.skwarczek.productlist.models.ShoppingCartModel
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object BackendHelper {

    fun addProductToBackend(product: ProductModel){

        val service = RetrofitService.create()
        val call = service.postProductCall(product)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful) {
                    Log.d("POST PRODUCT SUCCESS", response.message())
                } else {
                    Log.d("POST PRODUCT FAIL", response.message())
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("POST PRODUCT FAIL", t.message.toString())
            }
        })
    }

    fun deleteProductByProductIdAndCustomerIdFromBackend(productId: Int, customerId: String){
        val service = RetrofitService.create()
        val call = service.deleteProductByCustomerIdAndProductIdCall(customerId, productId)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if(response.isSuccessful) {
                    Log.d("DELETE PRODUCT SUCCESS", response.message())
                } else {
                    Log.d("DELETE PRODUCT FAIL", response.message())
                }
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d("DELETE PRODUCT FAIL", t.message.toString())
            }
        })
    }

    fun addShoppingCartToBackend(shoppingCart: ShoppingCartModel){

        val service = RetrofitService.create()
        val call = service.postShoppingCartCall(shoppingCart)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful) {
                    Log.d("POST SHOPPING_CART SUCCESS", response.message())
                } else {
                    Log.d("POST SHOPPING_CART FAIL", response.message())
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("POST SHOPPING_CART FAIL", t.message.toString())
            }
        })
    }

    fun placeOrderToBackend(randomId: Int, orderName: String, customerId: String, totalPrice: Double){

        val order = OrderModel(randomId, orderName, customerId, totalPrice)

        val service = RetrofitService.create()
        val call = service.postOrderAndOrderDetailsCall(order.id, order.name, order.customerId, order.totalPrice)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if(response.isSuccessful) {
                    Log.d("PLACE ORDER SUCCESS", response.message())
                } else {
                    Log.d("PLACE ORDER FAIL", response.message())
                }
            }
            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d("PLACE ORDER FAIL", t.message.toString())
            }
        })
    }

    fun deleteShoppingCartByCustomerIdAndProductIdFromBackend(customerId: String, productId: Int){
        val service = RetrofitService.create()
        val call = service.deleteShoppingCartByCustomerIdAndProductIdCall(customerId, productId)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if(response.isSuccessful) {
                    Log.d("DELETE CART SUCCESS", response.message())
                } else {
                    Log.d("DELETE CART FAIL", response.message())
                }
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d("DELETE CART FAIL", t.message.toString())
            }
        })
    }

    fun getOrdersByCustomerIdFromBackend(){

    }

}