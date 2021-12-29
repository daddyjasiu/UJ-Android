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

    fun addItems(items: ArrayList<ProductModel>){
        this.productsList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (ProductModel) -> Unit){
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.cart_item_fragment, parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productsList[position]
        holder.bindView(product)
        holder.itemView.setOnClickListener{onClickItem?.invoke(product)}
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class ProductViewHolder(var view: View) : RecyclerView.ViewHolder(view){

        private var id = view.findViewById<TextView>(R.id.product_id)
        private var name = view.findViewById<TextView>(R.id.product_name)
        private var description = view.findViewById<TextView>(R.id.product_description)
        private var deleteButton = view.findViewById<Button>(R.id.delete_cart_item_button)

        fun bindView(product: ProductModel){
            id.text = product.id.toString()
            name.text = product.name.toString()
            description.text = product.description.toString()
        }

    }
}