package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import android.os.Bundle
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
import pl.edu.uj.ii.skwarczek.productlist.utility.BackendHelper
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper
import kotlin.random.Random

class ProductListActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var wishName: EditText
    private lateinit var wishDescription: EditText
    private lateinit var makeAWishButton: Button
    private lateinit var goToCartButton: Button
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var settingsButton: Button
    private var productAdapter: ProductListAdapter? = null
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishmaking_screen)

        initView()
        initRecyclerView()

        //RealmHelper.syncDatabases(currentUser.uid)

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

                RealmHelper.addProduct(productRealm)
                BackendHelper.addProductToBackend(productBackend)
                getProductsByCustomerIdFromCache(currentUser.uid)
                clearEditText()
            }
        }

        goToCartButton.setOnClickListener{
            goToShoppingCart()
        }

        productAdapter?.setOnClickItem {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("${it.name}\n\n${it.description}")
            alert.setPositiveButton("OK"){ dialog, _ ->
                dialog.dismiss()
            }
            alert.setCancelable(true)
            alert.show()
        }

        productAdapter?.setOnClickAddToCartButton {
            addProductToCart(it)
        }

        productAdapter?.setOnClickDeleteButton {
            deleteProduct(it.id, currentUser.uid, true)
        }

        getProductsByCustomerIdFromCache(currentUser.uid)
    }

    private fun addProductToCart(product: ProductRealmModel){
        RealmHelper.addShoppingCart(ShoppingCartRealmModel(product.customerId, product.id, product.name, product.description))
        BackendHelper.addShoppingCartToBackend(ShoppingCartModel(product.customerId, product.id, product.name, product.description))
        deleteProduct(product.id, product.customerId, false)

        Toast.makeText(this, "Product added to shopping cart!", Toast.LENGTH_SHORT).show()
    }

    private fun getProductsByCustomerIdFromCache(customerId: String){
        val productList = RealmHelper.getAllProductsByCustomerId(customerId)

        for(product in productList){
            println(product.name)
        }

        productAdapter?.addItems(ArrayList(productList))
    }

    private fun deleteProduct(productId: Int, customerId: String, showAlert: Boolean){

        if(!showAlert){
            RealmHelper.deleteProductByProductIdAndCustomerId(productId, customerId)
            BackendHelper.deleteProductByProductIdAndCustomerIdFromBackend(productId, customerId)
            getProductsByCustomerIdFromCache(customerId)
        }
        else{
            val alert = AlertDialog.Builder(this)
            alert.setMessage("Are you sure you want to delete this wish?")
            alert.setCancelable(true)
            alert.setPositiveButton("Yes") { _, _ ->
                RealmHelper.deleteProductByProductIdAndCustomerId(productId, customerId)
                BackendHelper.deleteProductByProductIdAndCustomerIdFromBackend(productId, customerId)
                getProductsByCustomerIdFromCache(customerId)
                Toast.makeText(this, "Wish removed!", Toast.LENGTH_SHORT).show()
            }
            alert.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            alert.show()
        }
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

    private fun initView(){
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        wishName = findViewById(R.id.wish)
        wishDescription = findViewById(R.id.wish_description)
        makeAWishButton = findViewById(R.id.wish_button)
        goToCartButton = findViewById(R.id.go_to_cart_button)
        productsRecyclerView = findViewById(R.id.product_list_recycler_view)
        settingsButton = findViewById(R.id.settings_button)
    }

    private fun initRecyclerView(){
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductListAdapter()
        productsRecyclerView.adapter = productAdapter
    }
}