package pl.edu.uj.ii.skwarczek.productlist.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.ProductAdapter
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class ShoppingScreenActivity : AppCompatActivity() {

    private lateinit var wishName: EditText
    private lateinit var wishDescription: EditText
    private lateinit var wishButton: Button
    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var cartRecyclerView: RecyclerView
    private var productAdapter: ProductAdapter? = null
    private var product: ProductRealmModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_screen)

        initView()
        initRecyclerView()

        sqliteHelper = SQLiteHelper(this)

        getCartItemsFromCache()

        wishButton.setOnClickListener{
            addCartItemToCache()
            getCartItemsFromCache()
        }

        productAdapter?.setOnClickItem {
            wishName.setText(it.name)
            wishDescription.setText(it.description)
            product = it
        }

        productAdapter?.setOnClickAddToCartButton {
            //popup TODO
        }

        productAdapter?.setOnClickDeleteButton {
            deleteCartItem(it.id)
        }
    }

    private fun getCartItemsFromBackend() {
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: RetrofitService = retrofit.create(RetrofitService::class.java)
        val call = service.getProducts()

        call.enqueue(object : Callback<List<ProductRealmModel>> {
            override fun onResponse(call: Call<List<ProductRealmModel>>, response: Response<List<ProductRealmModel>>) {

                if (response.code() == 200) {

                    val productList = response.body()!!
                    productAdapter?.addItems(productList)

                    for(product in productList){
                        println("$product.id $product.name $product.description")
                    }
                }
            }
            override fun onFailure(call: Call<List<ProductRealmModel>>, t: Throwable) {
            }
        })
    }

    private fun addCartItemToCache() {
        val name = wishName.text.toString()
        val description = wishDescription.text.toString()

        if (name.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
        }
        else{
            val random = Random.nextInt(0, Int.MAX_VALUE)
            val product = ProductRealmModel(random, name, description)
            RealmHelper.addProductToDB(product)
            clearEditText()
        }
    }

    private fun getCartItemsFromCache(){
        val productList = RealmHelper.getAllProducts()
        productAdapter?.addItems(productList)
        println("PRINTING CURRENT CART ITEMS:")
        for(product in productList){
            println("$product.id, $product.name, $product.description")
        }
    }

    private fun deleteCartItem(id: Int){
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Are you sure you want to delete this wish?")
        alert.setCancelable(true)
        alert.setPositiveButton("Yes") { dialog, _ ->
            RealmHelper.deleteProductById(id)
            getCartItemsFromCache()
        }
        alert.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
    }

    fun settingsClicked(view: android.view.View) {
//        val intent = Intent(this,SettingsActivity::class.java)
//        startActivity(intent)
//        getCurrentData()
//        RealmHelper.clearDB()
        getCartItemsFromBackend()
        getCartItemsFromCache()
    }

    private fun clearEditText(){
        wishName.setText("")
        wishDescription.setText("")
        wishName.requestFocus()
    }

    private fun initRecyclerView(){
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter()
        cartRecyclerView.adapter = productAdapter
    }

    private fun initView(){
        wishName = findViewById(R.id.wish)
        wishDescription = findViewById(R.id.wish_description)
        wishButton = findViewById(R.id.wish_button)
        cartRecyclerView = findViewById(R.id.shopping_cart_recycler_view)
    }
}