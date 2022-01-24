package pl.edu.uj.ii.skwarczek.productlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.models.ProductModel
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>(){

    private var productsRealmList: ArrayList<ProductRealmModel> = arrayListOf()
    private var onClickItem: ((ProductRealmModel) -> Unit)? = null
    private var onClickAddToCartButton: ((ProductRealmModel) -> Unit)? = null
    private var onClickDeleteButton: ((ProductRealmModel) -> Unit)? = null

    fun addItems(items: ArrayList<ProductRealmModel>){
        this.productsRealmList.clear()
        this.productsRealmList = items
        notifyDataSetChanged()
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
        LayoutInflater.from(parent.context).inflate(R.layout.fragment_product_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productsRealmList[position]
        holder.bindView(product)
        holder.itemView.setOnClickListener{onClickItem?.invoke(product)}
        holder.addToCartButton.setOnClickListener{onClickAddToCartButton?.invoke(product)}
        holder.deleteButton.setOnClickListener{onClickDeleteButton?.invoke(product)}
    }

    override fun getItemCount(): Int {
        return productsRealmList.size
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private var name = view.findViewById<TextView>(R.id.product_list_item_product_name)
        private var description = view.findViewById<TextView>(R.id.product_list_item_product_description)
        var addToCartButton: Button = view.findViewById(R.id.add_to_cart_cart_item_button)
        var deleteButton: Button = view.findViewById(R.id.delete_cart_item_button)

        fun bindView(product: ProductRealmModel){
            name.text = product.name
            description.text = product.description
        }
    }
}