package pl.edu.uj.ii.skwarczek.productlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.models.OrderDetailsRealmModel

class OrderDetailsAdapter : RecyclerView.Adapter<OrderDetailsAdapter.ProductViewHolder>(){

    private var orderDetailsRealmList: ArrayList<OrderDetailsRealmModel> = arrayListOf()
    private var onClickItem: ((OrderDetailsRealmModel) -> Unit)? = null

    fun addItems(items: ArrayList<OrderDetailsRealmModel>){
        this.orderDetailsRealmList.clear()
        this.orderDetailsRealmList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (OrderDetailsRealmModel) -> Unit){
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.fragment_order_details_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val orderDetail = orderDetailsRealmList[position]
        holder.bindView(orderDetail)
        holder.itemView.setOnClickListener{onClickItem?.invoke(orderDetail)}
    }

    override fun getItemCount(): Int {
        return orderDetailsRealmList.size
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private var productName = view.findViewById<TextView>(R.id.order_details_list_item_product_name)
        private var productDescription = view.findViewById<TextView>(R.id.order_details_list_item_product_description)
        private var orderId = view.findViewById<TextView>(R.id.order_details_list_item_product_id)

        fun bindView(orderDetails: OrderDetailsRealmModel){
            productName.text = orderDetails.productName
            productDescription.text = orderDetails.productDescription
            orderId.text = orderDetails.orderId.toString()
        }
    }
}