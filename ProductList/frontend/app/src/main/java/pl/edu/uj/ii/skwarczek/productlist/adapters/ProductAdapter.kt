package pl.edu.uj.ii.skwarczek.productlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.callbackFlow
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.models.ProductModel

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    private var productsList: ArrayList<ProductModel> = ArrayList()
    private var onClickItem: ((ProductModel) -> Unit)? = null
    private var onClickUpdateButton: ((ProductModel) -> Unit)? = null
    private var onClickDeleteButton: ((ProductModel) -> Unit)? = null

    fun addItems(items: ArrayList<ProductModel>){
        this.productsList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (ProductModel) -> Unit){
        this.onClickItem = callback
    }

    fun setOnClickUpdateButton(callback: (ProductModel) -> Unit){
        this.onClickUpdateButton = callback
    }

    fun setOnClickDeleteButton(callback: (ProductModel) -> Unit){
        this.onClickDeleteButton = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.cart_item_fragment, parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productsList[position]
        holder.bindView(product)
        holder.itemView.setOnClickListener{onClickItem?.invoke(product)}
        holder.updateButton.setOnClickListener{onClickUpdateButton?.invoke(product)}
        holder.deleteButton.setOnClickListener{onClickDeleteButton?.invoke(product)}
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class ProductViewHolder(var view: View) : RecyclerView.ViewHolder(view){

        private var id = view.findViewById<TextView>(R.id.product_id)
        private var name = view.findViewById<TextView>(R.id.product_name)
        private var description = view.findViewById<TextView>(R.id.product_description)
        var updateButton = view.findViewById<Button>(R.id.update_cart_item_button)
        var deleteButton = view.findViewById<Button>(R.id.delete_cart_item_button)

        fun bindView(product: ProductModel){
            id.text = product.id.toString()
            name.text = product.name
            description.text = product.description
        }

    }
}