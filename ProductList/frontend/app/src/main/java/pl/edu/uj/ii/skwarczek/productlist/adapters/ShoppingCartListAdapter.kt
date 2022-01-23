package pl.edu.uj.ii.skwarczek.productlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.models.ShoppingCartRealmModel

class ShoppingCartListAdapter: RecyclerView.Adapter<ShoppingCartListAdapter.ShoppingCartViewHolder>() {
    private var cartsList: List<ShoppingCartRealmModel> = emptyList()
    private var onClickItem: ((ShoppingCartRealmModel) -> Unit)? = null
    private var onClickDeleteButton: ((ShoppingCartRealmModel) -> Unit)? = null

    fun addItems(items: List<ShoppingCartRealmModel>){
        this.cartsList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (ShoppingCartRealmModel) -> Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteButton(callback: (ShoppingCartRealmModel) -> Unit){
        this.onClickDeleteButton = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShoppingCartViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.fragment_shopping_cart_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val cart = cartsList[position]
        holder.bindView(cart)
        holder.itemView.setOnClickListener{onClickItem?.invoke(cart)}
        holder.deleteButton.setOnClickListener{onClickDeleteButton?.invoke(cart)}
    }

    override fun getItemCount(): Int {
        return cartsList.size
    }

    class ShoppingCartViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var productName = view.findViewById<TextView>(R.id.cart_item_product_name)
        private var productDescription = view.findViewById<TextView>(R.id.cart_item_product_description)
        var deleteButton: Button = view.findViewById(R.id.delete_cart_item_button)

        fun bindView(cart: ShoppingCartRealmModel){
            productName.text = cart.productName
            productDescription.text = cart.productDescription
        }

    }

}