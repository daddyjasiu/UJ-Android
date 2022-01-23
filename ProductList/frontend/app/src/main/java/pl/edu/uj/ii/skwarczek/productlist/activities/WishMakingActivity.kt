package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.ProductListAdapter
import pl.edu.uj.ii.skwarczek.productlist.models.*
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class WishMakingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var wishName: EditText
    private lateinit var wishDescription: EditText
    private lateinit var makeAWishButton: Button
    private lateinit var goToCartButton: Button
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var settingsButton: Button
    private var productAdapter: ProductListAdapter? = null
    private var product: ProductRealmModel? = null
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishmaking_screen)

        initView()
        initRecyclerView()

        getProductsByCustomerIdFromCache(currentUser.uid)

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        makeAWishButton.setOnClickListener{

            val name = wishName.text.toString()
            val description = wishDescription.text.toString()

            if (name.isEmpty() || description.isEmpty()){
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            }
            else{
                val random = Random.nextInt(0, Int.MAX_VALUE)
                val productRealm = ProductRealmModel(random, currentUser.uid, name, description)
                val productBackend = ProductModel(random, currentUser.uid, name, description)

                addProductToCache(productRealm)
                addProductToBackend(productBackend)
                getProductsByCustomerIdFromCache(currentUser.uid)
                clearEditText()
            }
        }

        goToCartButton.setOnClickListener{
            goToShoppingCart()
        }

        productAdapter?.setOnClickItem {

        }

        productAdapter?.setOnClickAddToCartButton {
            addProductToCart(it)
        }

        productAdapter?.setOnClickDeleteButton {
            deleteProduct(it.id, currentUser.uid, true)
        }
    }

    private fun addProductToCache(product: ProductRealmModel) {
        RealmHelper.addProduct(product)
    }

    private fun addProductToBackend(product: ProductModel){

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

    private fun addShoppingCartToBackend(shoppingCart: ShoppingCartModel){

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

    private fun addProductToCart(product: ProductRealmModel){
        RealmHelper.addShoppingCart(ShoppingCartRealmModel(product.customerId, product.id, product.name, product.description))
        addShoppingCartToBackend(ShoppingCartModel(product.customerId, product.id, product.name, product.description))
        deleteProduct(product.id, product.customerId, false)

        Toast.makeText(this, "Product added to shopping cart!", Toast.LENGTH_SHORT).show()
    }

    private fun getProductsByCustomerIdFromCache(customerId: String){
        val productList = RealmHelper.getAllProductsByCustomerId(customerId)
        productAdapter?.addItems(ArrayList(productList))
    }

    private fun deleteProduct(productId: Int, customerId: String, showAlert: Boolean){

        if(!showAlert){
            RealmHelper.deleteProductByProductIdAndCustomerId(productId, customerId)
            deleteProductByProductIdAndCustomerIdFromBackend(productId, customerId)
            getProductsByCustomerIdFromCache(customerId)
        }
        else{
            val alert = AlertDialog.Builder(this)
            alert.setMessage("Are you sure you want to delete this wish?")
            alert.setCancelable(true)
            alert.setPositiveButton("Yes") { dialog, _ ->
                RealmHelper.deleteProductByProductIdAndCustomerId(productId, customerId)
                deleteProductByProductIdAndCustomerIdFromBackend(productId, customerId)
                getProductsByCustomerIdFromCache(customerId)
                Toast.makeText(this, "Wish removed!", Toast.LENGTH_SHORT).show()
            }
            alert.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            alert.show()
        }
    }

    private fun deleteProductByProductIdAndCustomerIdFromBackend(productId: Int, customerId: String){
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

    private fun goToShoppingCart() {
        val intent = Intent(this,ShoppingCartActivity::class.java)
        startActivity(intent)
    }

    private fun clearEditText(){
        wishName.setText("")
        wishDescription.setText("")
        wishName.requestFocus()
    }

    private fun initRecyclerView(){
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductListAdapter()
        cartRecyclerView.adapter = productAdapter
    }

    private fun initView(){
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        wishName = findViewById(R.id.wish)
        wishDescription = findViewById(R.id.wish_description)
        makeAWishButton = findViewById(R.id.wish_button)
        goToCartButton = findViewById(R.id.go_to_cart_button)
        cartRecyclerView = findViewById(R.id.product_list_recycler_view)
        settingsButton = findViewById(R.id.settings_button)
    }
}