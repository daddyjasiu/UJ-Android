package pl.edu.uj.ii.skwarczek.productlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel
import pl.edu.uj.ii.skwarczek.productlist.models.ShoppingCartRealmModel
import pl.edu.uj.ii.skwarczek.productlist.utility.Globals
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper

class ShoppingCartAdapter: RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>() {
    private var cartsList: List<ShoppingCartRealmModel> = emptyList()
    private var onClickItem: ((ShoppingCartRealmModel) -> Unit)? = null

    fun addItems(items: List<ShoppingCartRealmModel>){
        this.cartsList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (ShoppingCartRealmModel) -> Unit){
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShoppingCartViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.shopping_cart_item_fragment, parent, false)
    )

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val cart = cartsList[position]
        holder.bindView(cart)
        holder.itemView.setOnClickListener{onClickItem?.invoke(cart)}
    }

    override fun getItemCount(): Int {
        return cartsList.size
    }

    class ShoppingCartViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var customerId = view.findViewById<TextView>(R.id.cart_item_customer_id)
        private var productId = view.findViewById<TextView>(R.id.cart_item_product_id)

        fun bindView(cart: ShoppingCartRealmModel){
            customerId.text = RealmHelper.getProductById(cart.productId)?.name
            productId.text = cart.productId.toString()
        }

    }

}