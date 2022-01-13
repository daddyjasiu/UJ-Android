package pl.edu.uj.ii.skwarczek.productlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    private var productsList: ArrayList<ProductRealmModel> = arrayListOf()
    private var onClickItem: ((ProductRealmModel) -> Unit)? = null
    private var onClickAddToCartButton: ((ProductRealmModel) -> Unit)? = null
    private var onClickDeleteButton: ((ProductRealmModel) -> Unit)? = null

    fun addItems(items: ArrayList<ProductRealmModel>){
        this.productsList = items
        notifyDataSetChanged()
    }

    fun deleteItemFromView(item: ProductRealmModel){
        if(this.productsList.contains(item)){
            this.productsList.remove(item)
            notifyDataSetChanged()
        }
    }

    fun setOnClickItem(callback: (ProductRealmModel) -> Unit){
        this.onClickItem = callback
    }

    fun setOnClickAddToCartButton(callback: (ProductRealmModel) -> Unit){
        this.onClickAddToCartButton = callback
    }

    fun setOnClickDeleteButton(callback: (ProductRealmModel) -> Unit){
        this.onClickDeleteButton = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.product_list_item_fragment, parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productsList[position]
        holder.bindView(product)
        holder.itemView.setOnClickListener{onClickItem?.invoke(product)}
        holder.addToCartButton.setOnClickListener{onClickAddToCartButton?.invoke(product)}
        holder.deleteButton.setOnClickListener{onClickDeleteButton?.invoke(product)}
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class ProductViewHolder(var view: View) : RecyclerView.ViewHolder(view){

        private var id = view.findViewById<TextView>(R.id.product_list_item_product_id)
        private var name = view.findViewById<TextView>(R.id.product_list_item_product_name)
        private var description = view.findViewById<TextView>(R.id.product_list_item_product_description)
        var addToCartButton: Button = view.findViewById<Button>(R.id.add_to_cart_cart_item_button)
        var deleteButton: Button = view.findViewById<Button>(R.id.delete_cart_item_button)

        fun bindView(product: ProductRealmModel){
            id.text = product.id.toString()
            name.text = product.name
            description.text = product.description
        }
    }
}