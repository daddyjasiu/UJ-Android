package pl.edu.uj.ii.skwarczek.productlist.services

import pl.edu.uj.ii.skwarczek.productlist.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.google.gson.GsonBuilder

import com.google.gson.Gson

interface RetrofitService {

    //    Products
    @GET("product")
    fun getProductsCall() : Call<List<ProductModel>>

    @GET("product/{customerId}")
    fun getProductsByCustomerIdCall(@Path("customerId") id : String) : Call<List<ProductModel>>

    @POST("product")
    fun postProductCall(@Body product: ProductModel) : Call<Unit>

    @PUT("product")
    fun updateProductCall(@Body product: ProductModel) : Call<Unit>

    @DELETE("product/{customerId}/{productId}")
    fun deleteProductByCustomerIdAndProductIdCall(@Path("customerId") customerId: String, @Path("productId") productId: Int) : Call<Unit>

    @DELETE("product")
    fun deleteAllProductsCall() : Call<Unit>

    //    Customer
    @GET("customer")
    fun getAllCustomersCall() : Call<List<CustomerModel>>

    @GET("customer/{id}")
    fun getCustomerByIdCall(@Path("id") id : String) : Call<CustomerModel>

    @GET("customer/{email}/{password}")
    fun getCustomerByEmailAndPasswordCall(@Path("email") email: String, @Path("password") password: String) : Call<CustomerModel>

    @POST("customer")
    fun postCustomerCall(@Body customer : CustomerModel) : Call<Unit>

    @PUT("customer")
    fun putCustomerCall(@Body customer : CustomerModel) : Call<Unit>

    @DELETE("customer/{customerId}")
    fun deleteCustomerCall(@Path("customerId") customerId: String) : Call<Unit>

    @DELETE("customer")
    fun deleteAllCustomersCall() : Call<Unit>

    //    Cart
    @GET("cart")
    fun getAllShoppingCartsCall() : Call<List<ShoppingCartModel>>

    @GET("cart/{customerId}")
    fun getShoppingCartsByCustomerIdCall(@Path("customerId") customerId : String) : Call<List<ShoppingCartModel>>

    @POST("cart")
    fun postShoppingCartCall(@Body cart: ShoppingCartModel) : Call<Unit>

    @PUT("cart")
    fun putShoppingCartCall(@Body cart: ShoppingCartModel) : Call<Unit>

    @DELETE("cart/{customerId}/{productId}")
    fun deleteShoppingCartByCustomerIdAndProductIdCall(@Path("customerId") customerId: String, @Path("productId") productId: Int) : Call<Unit>

    @DELETE("cart/{customerId}")
    fun deleteShoppingCartsByCustomerIdCall(@Path("customerId") customerId: String) : Call<Unit>

    @DELETE("cart")
    fun deleteAllShoppingCarts() : Call<Unit>

    //    Orders
    @GET("order")
    fun getAllOrdersCall() : Call<List<OrderModel>>

    @GET("order/customer/{customerId}")
    fun getOrdersByCustomerIdCall(@Path("customerId") customerId: String) : Call<List<OrderModel>>

    @POST("order/{orderId}/{orderName}/{customerId}/{totalPrice}")
    fun postOrderAndOrderDetailsCall(@Path("orderId") orderId: Int, @Path("orderName") orderName: String, @Path("customerId") customerId: String, @Path("totalPrice") totalPrice: Double) : Call<Unit>

    @DELETE("order/{id}")
    fun deleteOrderByIdCall(@Path("id") orderId: Int) : Call<Unit>

    @DELETE("order")
    fun deleteAllOrders() : Call<Unit>

    //    Order details
    @GET("orderDetails/{orderId}")
    fun getOrderDetailsByIdCall(@Path("orderId") orderId : Int) : Call<List<OrderDetailsModel>>

    @GET("orderDetails/customer/{customerId}")
    fun getCustomerOrderDetailsCall(@Path("customerId") customerId: String) : Call<List<OrderDetailsModel>>


    companion object {

        var BASE_URL = "https://e0d6-185-58-160-75.ngrok.io"

        fun create() : RetrofitService {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(RetrofitService::class.java)

        }
    }
}