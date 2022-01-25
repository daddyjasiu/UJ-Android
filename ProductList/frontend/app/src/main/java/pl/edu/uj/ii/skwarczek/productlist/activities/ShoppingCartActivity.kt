package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.ShoppingCartListAdapter
import pl.edu.uj.ii.skwarczek.productlist.models.*
import pl.edu.uj.ii.skwarczek.productlist.utility.BackendHelper
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper
import kotlin.random.Random

class ShoppingCartActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var cartRecyclerView: RecyclerView
    private var shoppingCartAdapter: ShoppingCartListAdapter? = null
    private lateinit var goToOrdersButton: Button
    private lateinit var placeOrderButton: Button
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        initView()
        initActionBar()
        initRecyclerView()

        placeOrderButton.setOnClickListener {
            placeOrder()
        }

        goToOrdersButton.setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
        }

        shoppingCartAdapter?.setOnClickDeleteButton {
            deleteShoppingCartItem(it.customerId, it.productId)
        }

        shoppingCartAdapter?.setOnClickItem {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("${it.productName}\n\n${it.productDescription}")
            alert.setPositiveButton("OK"){ dialog, _ ->
                dialog.dismiss()
            }
            alert.setCancelable(true)
            alert.show()
        }

        getShoppingCartItemsByCustomerIdFromCache(currentUser.uid)
    }

    private fun initActionBar(){
        val actionBar = supportActionBar
        actionBar!!.title = "Your shopping cart"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun placeOrder(){

        val alert = AlertDialog.Builder(this)
        val input = EditText(this)

        input.hint = "Order name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        alert.setView(input)

        alert.setTitle("Are you sure you want to place an order?")
        alert.setCancelable(true)

        alert.setPositiveButton("Yes") { _, _ ->
            val orderNameText = input.text.toString()

            if(orderNameText.isNotEmpty()){
                val cartList = RealmHelper.getShoppingCartsByCustomerId(currentUser.uid)
                val randomId = Random.nextInt(0, Int.MAX_VALUE)

                placeOrderToCache(randomId, orderNameText, cartList)
                BackendHelper.placeOrderToBackend(randomId, orderNameText, currentUser.uid, -1.0)
                getShoppingCartItemsByCustomerIdFromCache(currentUser.uid)

                Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Please enter order name", Toast.LENGTH_SHORT).show()
            }
        }
        alert.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
    }

    private fun placeOrderToCache(randomId: Int, orderName: String, cartList: List<ShoppingCartRealmModel>){

        val order = OrderRealmModel(randomId, orderName, currentUser.uid)
        RealmHelper.placeOrder(order)

        for(cart in cartList){
            val orderDetails = OrderDetailsRealmModel(order.id, cart.productId, cart.productName, cart.productDescription)
            RealmHelper.placeOrderDetails(orderDetails)
        }
        RealmHelper.deleteAllShoppingCartsByCustomerId(currentUser.uid)
    }

    private fun getShoppingCartItemsByCustomerIdFromCache(customerId: String){
        val cartList = RealmHelper.getShoppingCartsByCustomerId(customerId)
        shoppingCartAdapter?.addItems(cartList)
    }

    private fun deleteShoppingCartItem(customerId: String, productId: Int){

        val alert = AlertDialog.Builder(this)
        alert.setMessage("Are you sure you want to delete this wish from your shopping cart? \n\nIt will be lost forever!")
        alert.setCancelable(true)
        alert.setPositiveButton("Yes") { _, _ ->
            RealmHelper.deleteShoppingCartByCustomerIdAndProductId(customerId, productId)
            BackendHelper.deleteShoppingCartByCustomerIdAndProductIdFromBackend(customerId, productId)
            getShoppingCartItemsByCustomerIdFromCache(currentUser.uid)
            Toast.makeText(this, "Wish removed from shopping cart!", Toast.LENGTH_SHORT).show()
        }
        alert.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()

    }

    private fun initView(){
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        cartRecyclerView = findViewById(R.id.shopping_cart_recycler_view)
        placeOrderButton = findViewById(R.id.place_order_button)
        goToOrdersButton = findViewById(R.id.go_to_orders_button)
    }

    private fun initRecyclerView(){
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        shoppingCartAdapter = ShoppingCartListAdapter()
        cartRecyclerView.adapter = shoppingCartAdapter
    }

}