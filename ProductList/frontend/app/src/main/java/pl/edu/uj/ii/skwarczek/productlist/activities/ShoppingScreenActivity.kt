package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.ProductAdapter
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel
import pl.edu.uj.ii.skwarczek.productlist.utility.Globals
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper
import kotlin.random.Random

class ShoppingScreenActivity : AppCompatActivity() {

    private lateinit var wishName: EditText
    private lateinit var wishDescription: EditText
    private lateinit var wishButton: Button
    private lateinit var goToCartButton: Button
    private lateinit var cartRecyclerView: RecyclerView
    private var productAdapter: ProductAdapter? = null
    private var product: ProductRealmModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishmaking_screen)

        initView()
        initRecyclerView()

        getProductsFromCache()

        wishButton.setOnClickListener{
            addProductToCache()
            getProductsFromCache()
        }

        goToCartButton.setOnClickListener{
            goToShoppingCart()
        }

        productAdapter?.setOnClickItem {
            wishName.setText(it.name)
            wishDescription.setText(it.description)
            product = it
        }

        productAdapter?.setOnClickAddToCartButton {
            addProductToCart(it)
        }

        productAdapter?.setOnClickDeleteButton {
            deleteProductById(it.id, true)
        }
    }

    private fun addProductToCache() {
        val name = wishName.text.toString()
        val description = wishDescription.text.toString()

        if (name.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
        }
        else{
            val random = Random.nextInt(0, Int.MAX_VALUE)
            val product = ProductRealmModel(random, Globals.getCurrentUser()!!.id, name, description)
            RealmHelper.addProduct(product)
            clearEditText()

        }
    }

    private fun addProductToCart(product: ProductRealmModel){
        //RealmHelper.addProductToCart(product)

        //deleteProductById(product.id, false)
        Toast.makeText(this, "Product added to shopping cart!", Toast.LENGTH_SHORT).show()
    }

    private fun getProductsFromCache(){
        val productList = RealmHelper.getAllProducts()
        productAdapter?.addItems(ArrayList(productList))
    }

    private fun deleteProductById(id: Int, showAlert: Boolean){

        if(!showAlert){
            RealmHelper.deleteProductById(id)
            getProductsFromCache()
        }
        else{
            val alert = AlertDialog.Builder(this)
            alert.setMessage("Are you sure you want to delete this wish?")
            alert.setCancelable(true)
            alert.setPositiveButton("Yes") { dialog, _ ->
                RealmHelper.deleteProductById(id)
                getProductsFromCache()
            }
            alert.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            alert.show()
        }
    }

    fun settingsClicked(view: android.view.View) {
//        val intent = Intent(this,SettingsActivity::class.java)
//        startActivity(intent)
//        getCurrentData()
//        RealmHelper.clearDB()
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
        productAdapter = ProductAdapter()
        cartRecyclerView.adapter = productAdapter
    }

    private fun initView(){
        wishName = findViewById(R.id.wish)
        wishDescription = findViewById(R.id.wish_description)
        wishButton = findViewById(R.id.wish_button)
        goToCartButton = findViewById(R.id.go_to_cart_button)
        cartRecyclerView = findViewById(R.id.product_list_recycler_view)
    }
}