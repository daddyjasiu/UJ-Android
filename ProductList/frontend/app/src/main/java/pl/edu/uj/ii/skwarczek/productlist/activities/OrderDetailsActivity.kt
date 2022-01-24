package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.OrderDetailsAdapter
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var orderDetailsRecyclerView: RecyclerView
    private var orderDetailsAdapter: OrderDetailsAdapter? = null
    private lateinit var backArrowButton: Button
    private var orderId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        initView()
        initRecyclerView()

        orderDetailsAdapter?.setOnClickItem {
            val alert = AlertDialog.Builder(this)
            alert.setMessage("${it.productName}\n\n${it.productDescription}")
            alert.setPositiveButton("OK"){ dialog, _ ->
                dialog.dismiss()
            }
            alert.setCancelable(true)
            alert.show()
        }

        backArrowButton.setOnClickListener{
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
            finish()
        }

        orderId = intent.getIntExtra("orderId", orderId)

        getOrderDetailsByCustomerIdFromCache(orderId)
    }

    private fun getOrderDetailsByCustomerIdFromCache(orderId: Int){
        val orderDetailsList = RealmHelper.getOrderDetailsByOrderId(orderId)

        for(orderDetails in orderDetailsList){
            println(orderDetails.productName)
        }

        orderDetailsAdapter?.addItems(ArrayList(orderDetailsList))
    }

    private fun initView(){
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        orderDetailsRecyclerView = findViewById(R.id.order_details_list_recycler_view)
        backArrowButton = findViewById(R.id.back_arrow_order_details_button)
    }

    private fun initRecyclerView(){
        orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        orderDetailsAdapter = OrderDetailsAdapter()
        orderDetailsRecyclerView.adapter = orderDetailsAdapter
    }
}