package pl.edu.uj.ii.skwarczek.productlist.utility

import pl.edu.uj.ii.skwarczek.productlist.models.CustomerRealmModel
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel

object Globals {
    private var currentUser = CustomerRealmModel()
    private val productsInShoppingCart = ArrayList<ProductRealmModel>()

    fun setCurrentUser(user: CustomerRealmModel){
        currentUser = user
    }

    fun getCurrentUser(): CustomerRealmModel{
        return currentUser
    }

    fun addToShoppingCart(product: ProductRealmModel){
        productsInShoppingCart.add(product)
    }

    fun removeFromShoppingCartById(id: Int){
        for(product in productsInShoppingCart){
            if(product.id == id)
                productsInShoppingCart.remove(product)
        }
    }

    fun getProductsInShoppingCart(): ArrayList<ProductRealmModel>{
        return productsInShoppingCart
    }
}