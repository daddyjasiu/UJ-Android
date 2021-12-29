package pl.edu.uj.ii.skwarczek.productlist.services

import pl.edu.uj.ii.skwarczek.productlist.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    //Products
    @GET("product")
    fun getProductsCall() : Call<List<ProductModel>>

    @POST("product")
    fun postProductCall(@Body product : ProductModel) : Call<ProductModel>

    @PUT("product")
    fun putProductCall(@Body product : ProductModel) : Call<ProductModel>

    //Customer
    @GET("customer/{id}")
    fun getCustomerByIdCall(@Path("id") id : String) : Call<CustomerModel>

    @POST("customer")
    fun postCustomerCall(@Body customer : CustomerModel) : Call<CustomerModel>

    @PUT("customer")
    fun putCustomerCall(@Body customer : CustomerModel) : Call<CustomerModel>

    @DELETE("customer/{customerId}")
    fun deleteCustomerCall(@Path("customerId") customerId: String) : Call<CustomerModel>

    //Cart
    @GET("cart/{customerId}")
    fun getCartByIdCall(@Path("customerId") customerId : String) : Call<List<ShoppingCartModel>>

    @POST("cart/{customerId}/{productId}")
    fun postCartItemCall(@Path("customerId") customerId: String, @Path("productId") productId : Int) : Call<ShoppingCartModel>

    @DELETE("cart/{customerId}/{productId}")
    fun deleteCartItemCall(@Path("customerId") customerId: String, @Path("productId") productId: Int) : Call<ShoppingCartModel>

    @DELETE("cart/{customerId}")
    fun deleteCartCall(@Path("customerId") customerId: String) : Call<List<ShoppingCartModel>>

    //Orders
    @GET("order/customer/{customerId}")
    fun getCustomerOrdersCall(@Path("customerId") customerId: String) : Call<List<OrderModel>>

    @POST("order/{customerId}")
    fun postCustomerOrderCall(@Path("customerId") customerId: String) : Call<OrderModel>

    //Order details
    @GET("oderDetails/{orderId}")
    fun getOrderDetailsByIdCall(@Path("orderId") orderId : Int) : Call<List<OrderDetailsModel>>

    @GET("orderDetails/customer/{customerId}")
    fun getCustomerOrderDetailsCall(@Path("customerId") customerId: String) : Call<List<OrderDetailsModel>>


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