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
import pl.edu.uj.ii.skwarczek.productlist.adapters.OrdersListAdapter

class OrdersActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var ordersRecyclerView: RecyclerView
    private var ordersAdapter: OrdersListAdapter? = null
    private lateinit var backArrowButton: Button
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        initView()
        initRecyclerView()

        backArrowButton.setOnClickListener{
            val intent = Intent(this, ShoppingCartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun initView(){
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        ordersRecyclerView = findViewById(R.id.orders_recycler_view)
        backArrowButton = findViewById(R.id.back_arrow_orders_button)
    }

    private fun initRecyclerView(){
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter = OrdersListAdapter()
        ordersRecyclerView.adapter = ordersAdapter
    }

}