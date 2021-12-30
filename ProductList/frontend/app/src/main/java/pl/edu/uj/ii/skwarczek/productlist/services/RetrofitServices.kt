package pl.edu.uj.ii.skwarczek.productlist.services

import pl.edu.uj.ii.skwarczek.productlist.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    //Products
    @GET("product")
    fun getProducts() : Call<List<ProductModel>>

    @GET("product/{id}")
    fun getProduct(@Path("id") id: Int) : Call<ProductModel>

    @POST("product")
    fun createProduct(@Body product: ProductModel) : Call<ProductModel>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id") id: Int) : Call<ProductModel>

    //Customer
    @GET("customer")
    fun getCustomers() : Call<List<CustomerModel>>

    @GET("user/{id}")
    fun getCustomer(@Path("id") id: Int) : Call<CustomerModel>

    @POST("user")
    fun createCustomer(@Body product: CustomerModel) : Call<CustomerModel>

    @DELETE("user/{id}")
    fun deleteUser(@Path("id") id: Int) : Call<CustomerModel>

    //Cart
    @GET("cart/{customerId}")
    fun getCartById(@Path("customerId") customerId : String) : Call<List<ShoppingCartModel>>

    @POST("cart/{customerId}/{productId}")
    fun postCartItem(@Path("customerId") customerId: String, @Path("productId") productId : Int) : Call<ShoppingCartModel>

    @DELETE("cart/{customerId}/{productId}")
    fun deleteCartItem(@Path("customerId") customerId: String, @Path("productId") productId: Int) : Call<ShoppingCartModel>

    @DELETE("cart/{customerId}")
    fun deleteCart(@Path("customerId") customerId: String) : Call<List<ShoppingCartModel>>

    //Orders
    @GET("order/customer/{customerId}")
    fun getCustomerOrders(@Path("customerId") customerId: String) : Call<List<OrderModel>>

    @POST("order/{customerId}")
    fun postCustomerOrder(@Path("customerId") customerId: String) : Call<OrderModel>

    //Order details
    @GET("oderDetails/{orderId}")
    fun getOrderDetailsById(@Path("orderId") orderId : Int) : Call<List<OrderDetailsModel>>

    @GET("orderDetails/customer/{customerId}")
    fun getCustomerOrderDetails(@Path("customerId") customerId: String) : Call<List<OrderDetailsModel>>


    companion object {

        var BASE_URL = "http://localhost:8080/"

        fun create() : RetrofitService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(RetrofitService::class.java)

        }
    }
}