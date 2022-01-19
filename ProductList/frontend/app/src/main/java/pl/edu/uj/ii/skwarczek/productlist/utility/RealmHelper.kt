package pl.edu.uj.ii.skwarczek.productlist.utility

import io.realm.Realm

import io.realm.RealmResults
import pl.edu.uj.ii.skwarczek.productlist.models.*
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RealmHelper {

    private var realm: Realm = Realm.getDefaultInstance()

    fun clearDB(){
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

    fun addShoppingCart(shoppingCart: ShoppingCartModel){

        val cart = ShoppingCartRealmModel(shoppingCart.customerId, shoppingCart.productId, shoppingCart.productName, shoppingCart.productDescription)
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            bgRealm.insert(cart)

        }, Realm.Transaction.OnSuccess {
            println("Cart added to local Realm database with credentials:")
            println("PRODUCT_ID: ${cart.productId}, CUSTOMER_ID: ${cart.customerId}")
        })
    }

    fun getAllShoppingCarts(): List<ShoppingCartRealmModel>{

        return Realm.getDefaultInstance()
            .where(ShoppingCartRealmModel::class.java)
            .findAll()
    }

    fun deleteAllShoppingCartsByUserId(id: Int){
        realm.executeTransaction{
            val rows: RealmResults<ShoppingCartRealmModel> =
                realm.where(ShoppingCartRealmModel::class.java).equalTo("customerId", id).findAll()
            rows.deleteAllFromRealm()
        }
    }

    fun addProduct(product: ProductRealmModel){
        realm.executeTransaction { bgRealm ->
            bgRealm.insert(product)
        }
    }

    fun getProductById(id: Int): ProductRealmModel?{
        return Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .equalTo("id", id)
            .findFirst()
    }

    fun getAllProducts(): MutableList<ProductRealmModel> {

        return Realm.getDefaultInstance()
            .where(ProductRealmModel::class.java)
            .findAll()
    }

    fun deleteProductById(id: Int){
        realm.executeTransaction{
            val rows: RealmResults<ProductRealmModel> =
                realm.where(ProductRealmModel::class.java).equalTo("id", id).findAll()
            rows.deleteAllFromRealm()
        }

    }

    fun addCustomer(customer: CustomerRealmModel){
        realm.executeTransaction { bgRealm ->
            bgRealm.insert(customer)
        }
    }

    fun getCustomerById(id: Int): CustomerRealmModel? {

        return Realm.getDefaultInstance()
            .where(CustomerRealmModel::class.java)
            .equalTo("id", id)
            .findFirst()
    }

    fun getCustomerByEmailAndPassword(email: String, password: String): CustomerRealmModel? {

        return Realm.getDefaultInstance()
            .where(CustomerRealmModel::class.java)
            .equalTo("email", email)
            .and()
            .equalTo("password", password)
            .findFirst()
    }

    fun checkIfCustomerExistsById(id: Int): Boolean{

        val customer = Realm.getDefaultInstance()
            .where(CustomerRealmModel::class.java)
            .equalTo("id", id)
            .findFirst()

        return customer == null
    }

    fun syncRealmWithSQLite(customerId: Int){
        getProductsByCustomerIdFromSQL(customerId)
        getShoppingCartsByCustomerIdFromSQL(customerId)
    }

    private fun getCurrentCustomerByIdFromSQL(id: Int): CustomerModel {

        var customer = CustomerModel(0, "", "", "", "")
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: RetrofitService = retrofit.create(RetrofitService::class.java)
        val call = service.getCustomerByIdCall(id)

        call.enqueue(object : Callback<CustomerModel> {
            override fun onResponse(call: Call<CustomerModel>, response: Response<CustomerModel>) {

                if (response.code() == 200) {

                    val currentCustomer =  response.body()!!

                    customer = CustomerModel(
                        currentCustomer.id,
                        currentCustomer.firstName,
                        currentCustomer.lastName,
                        currentCustomer.email,
                        currentCustomer.password
                    )
                }
            }

            override fun onFailure(call: Call<CustomerModel>, t: Throwable) {
                println("DB SYNC: CUSTOMER SYNC FAILED, NO CUSTOMER WITH GIVEN ID FOUND")
            }
        })

        return customer
    }

    fun getCurrentCustomerByNameAndPasswordFromSQL(name: String, password: String) {

        val service = RetrofitService.create()
        val call = service.getCustomerByEmailAndPasswordCall(name, password)

        call.enqueue(object : Callback<CustomerModel> {
            override fun onResponse(call: Call<CustomerModel>, response: Response<CustomerModel>) {

                if (response.isSuccessful && response.body() != null) {

                    val currentCustomer = response.body()!!
                    println("${currentCustomer.id}, ${currentCustomer.email}")
                    addCustomer(CustomerRealmModel(
                        currentCustomer.id,
                        currentCustomer.firstName,
                        currentCustomer.lastName,
                        currentCustomer.email,
                        currentCustomer.password
                    ))
                }
            }

            override fun onFailure(call: Call<CustomerModel>, t: Throwable) {
                println("DB SYNC: CUSTOMER SYNC FAILED, NO CUSTOMER WITH GIVEN NAME AND PASSWORD FOUND")
                println(t.message)
            }
        })
    }

    private fun getProductsByCustomerIdFromSQL(customerId: Int){
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: RetrofitService = retrofit.create(RetrofitService::class.java)
        val call = service.getProductsByCustomerIdCall(customerId)

        call.enqueue(object : Callback<List<ProductModel>> {
            override fun onResponse(call: Call<List<ProductModel>>, response: Response<List<ProductModel>>) {

                if (response.code() == 200) {

                    val productList = response.body()!!

                    for(product in productList){
                        addProduct(
                            ProductRealmModel(
                            product.id,
                            product.customerId,
                            product.name,
                            product.description,
                        )
                        )
                    }
                }
            }
            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                println("DB SYNC: PRODUCTS BY CUSTOMER_ID SYNC FAILED, NO PRODUCTS WITH GIVEN ID FOUND")
            }
        })
    }

    private fun getShoppingCartsByCustomerIdFromSQL(customerId: Int){
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: RetrofitService = retrofit.create(RetrofitService::class.java)
        val call = service.getShoppingCartsByCustomerIdCall(customerId)

        call.enqueue(object : Callback<List<ShoppingCartModel>> {
            override fun onResponse(call: Call<List<ShoppingCartModel>>, response: Response<List<ShoppingCartModel>>) {

                if (response.code() == 200) {

                    val cartList = response.body()!!

                    for(cart in cartList){
                        addShoppingCart(cart)
                    }
                }
            }
            override fun onFailure(call: Call<List<ShoppingCartModel>>, t: Throwable) {
                println("DB SYNC: SHOPPING_CARTS BY CUSTOMER_ID SYNC FAILED, NO CARTS WITH GIVEN ID FOUND")
            }
        })

    }

    private fun getOrdersFromSQL(){

    }

    private fun getOrderDetailsFromSQL(){

    }

}