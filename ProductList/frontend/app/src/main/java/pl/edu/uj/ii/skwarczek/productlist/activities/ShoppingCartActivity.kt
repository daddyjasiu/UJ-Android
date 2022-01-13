package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.ShoppingCartAdapter
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel
import pl.edu.uj.ii.skwarczek.productlist.utility.Globals
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper

class ShoppingCartActivity: AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private var shoppingCartAdapter: ShoppingCartAdapter? = null
    private lateinit var backArrowButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        initView()
        initRecyclerView()

        backArrowButton.setOnClickListener{
            RealmHelper.deleteAllShoppingCartsByUserId(Globals.getCurrentUser().id)
            val intent = Intent(this, ShoppingScreenActivity::class.java)
            startActivity(intent)
        }
        getCartItems()
    }

    private fun getCartItems(){
        val cartList = RealmHelper.getAllShoppingCarts()
        shoppingCartAdapter?.addItems(cartList)
    }

    private fun initView(){
        cartRecyclerView = findViewById(R.id.shopping_cart_recycler_view)
        backArrowButton = findViewById(R.id.back_arrow_cart_button)
    }

    private fun initRecyclerView(){
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        shoppingCartAdapter = ShoppingCartAdapter()
        cartRecyclerView.adapter = shoppingCartAdapter
    }

}