package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.OrderListAdapter
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper

class OrdersActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var ordersRecyclerView: RecyclerView
    private var ordersAdapter: OrderListAdapter? = null
    private lateinit var backArrowButton: Button
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        initView()
        initRecyclerView()

        ordersAdapter?.setOnClickItem {
            val intent = Intent(this, OrderDetailsActivity::class.java)
            intent.putExtra("orderId", it.id)
            startActivity(intent)
        }

        backArrowButton.setOnClickListener{
            val intent = Intent(this, ShoppingCartActivity::class.java)
            startActivity(intent)
            finish()
        }

        getOrdersByCustomerIdFromCache()
    }


    private fun getOrdersByCustomerIdFromCache(){
        val ordersList = RealmHelper.getOrdersByCustomerId(currentUser.uid)
        ordersAdapter?.addItems(ordersList)
    }

    private fun initView(){
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        ordersRecyclerView = findViewById(R.id.orders_recycler_view)
        backArrowButton = findViewById(R.id.back_arrow_orders_button)
    }

    private fun initRecyclerView(){
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter = OrderListAdapter()
        ordersRecyclerView.adapter = ordersAdapter
    }

}