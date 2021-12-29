package pl.edu.uj.ii.skwarczek.productlist.services

import pl.edu.uj.ii.skwarczek.productlist.data.ShoppingCart
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ShoppingCartService {
    @GET("cart/{customerId}")
    fun getCartByIdCall(@Path("customerId") customerId : Int) : Call<List<ShoppingCart>>

    @POST("cart/{customerId}/{productId}")
    fun postCartItemCall(@Path("customerId") customerId: Int, @Path("productId") productId : Int) : Call<ShoppingCart>

    @DELETE("cart/{customerId}/{productId}")
    fun deleteCartItemCall(@Path("customerId") customerId: Int, @Path("productId") productId: Int) : Call<ShoppingCart>

    @DELETE("cart/{customerId}")
    fun deleteCartCall(@Path("customerId") customerId: Int) : Call<List<ShoppingCart>>

    companion object {

        //var BASE_URL = "https://1bae-185-25-121-195.ngrok.io/"
        private var BASE_URL = "http://10.0.2.2:8080/"

        fun create() : ShoppingCartService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ShoppingCartService::class.java)

        }
    }
}