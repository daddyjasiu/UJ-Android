package pl.edu.uj.ii.skwarczek.productlist.services
import pl.edu.uj.ii.skwarczek.productlist.data.Customer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CustomerService{
    @GET("customer/{id}")
    fun getCustomerByIdCall(@Path("id") id : Int) : Call<Customer>

    @POST("customer")
    fun postCustomerCall(@Body customer : Customer) : Call<Customer>

    @DELETE("customer/{customerId}")
    fun deleteCustomerCall(@Path("customerId") customerId: Int) : Call<Customer>

    companion object {

        //var BASE_URL = "https://1bae-185-25-121-195.ngrok.io/"
        private var BASE_URL = "http://10.0.2.2:8080/"

        fun create() : CustomerService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(CustomerService::class.java)

        }
    }
}