package pl.edu.uj.ii.skwarczek.productlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.models.OrderRealmModel

class OrdersListAdapter: RecyclerView.Adapter<OrdersListAdapter.OrderViewHolder>() {
    private var ordersList: List<OrderRealmModel> = emptyList()
    private var onClickItem: ((OrderRealmModel) -> Unit)? = null

    fun addItems(items: List<OrderRealmModel>){
        this.ordersList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (OrderRealmModel) -> Unit){
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.fragment_orders_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.bindView(order)
        holder.itemView.setOnClickListener{onClickItem?.invoke(order)}
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var orderId = view.findViewById<TextView>(R.id.orders_list_item_order_id)
        private var orderTotalPrice = view.findViewById<TextView>(R.id.orders_list_item_total_price)

        fun bindView(order: OrderRealmModel){
            orderId.text = order.id.toString()
            orderTotalPrice.text = order.totalPrice.toString()
        }
    }
}