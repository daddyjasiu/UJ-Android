package pl.edu.uj.ii.skwarczek.productlist.services

import pl.edu.uj.ii.skwarczek.productlist.models.ProductModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ProductService{
    @GET("product")
    fun getProductsCall() : Call<List<ProductModel>>

    companion object {

        //var BASE_URL = "https://1bae-185-25-121-195.ngrok.io/"
        private var BASE_URL = "http://10.0.2.2:8080/"

        fun create() : ProductService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ProductService::class.java)

        }
    }
}