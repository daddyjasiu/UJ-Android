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
import pl.edu.uj.ii.skwarczek.productlist.models.ProductModel

class ShoppingScreenActivity : AppCompatActivity() {

    private lateinit var wishName: EditText
    private lateinit var wishDescription: EditText
    private lateinit var wishButton: Button
    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var updateButton: Button
    private var productAdapter: ProductAdapter? = null
    private var product: ProductModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_screen)

        initView()
        initRecyclerView()

        sqliteHelper = SQLiteHelper(this)

        getProducts()

        wishButton.setOnClickListener{
            addProduct()
            getProducts()
        }

        productAdapter?.setOnClickItem {

            wishName.setText(it.name)
            wishDescription.setText(it.description)
            product = it
        }

        productAdapter?.setOnClickUpdateButton {
            //popup TODO
        }

        productAdapter?.setOnClickDeleteButton {
            deleteProduct(it.id)
        }

    }

    fun settingsClicked(view: android.view.View) {
        val intent = Intent(this,SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun addProduct() {
        val name = wishName.text.toString()
        val description = wishDescription.text.toString()

        if (name.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
        }
        else{
            val product = ProductModel(name = name, description = description)
            val status = sqliteHelper.insertProduct(product)

            if(status > -1){
                Toast.makeText(this, "Wish added!", Toast.LENGTH_SHORT).show()
                clearEditText()
            }
            else{
                Toast.makeText(this, "Wish lost!", Toast.LENGTH_SHORT).show()
                clearEditText()
            }
        }
    }

    private fun getProducts(){
        val productList = sqliteHelper.getAllProducts()
        productAdapter?.addItems(productList)
        for(product in productList){
            println("$product.id, $product.name, $product.description")
        }
    }

    private fun updateProduct(){
        val name = wishName.text.toString()
        val description = wishDescription.text.toString()

        if(name==product?.name && description==product?.description) {
            Toast.makeText(this, "Please type in new name or description", Toast.LENGTH_SHORT)
                .show()
        }
        else{
            if(product != null) {
                val newProduct =
                    ProductModel(id = product!!.id, name = name, description = description)
                val status = sqliteHelper.updateProduct(newProduct)
                if(status > -1){
                    clearEditText()
                    getProducts()
                }
            }
        }
    }

    private fun deleteProduct(id: Int){
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Are you sure you want to delete this wish?")
        alert.setCancelable(true)
        alert.setPositiveButton("Yes") { dialog, _ ->
            sqliteHelper.deleteProductById(id)
            getProducts()
        }
        alert.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
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