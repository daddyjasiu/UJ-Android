package pl.edu.uj.ii.skwarczek.productlist.utility

import io.realm.Realm
import pl.edu.uj.ii.skwarczek.productlist.models.CustomerModel
import pl.edu.uj.ii.skwarczek.productlist.models.CustomerRealmModel
import pl.edu.uj.ii.skwarczek.productlist.models.ProductRealmModel

object Globals {
    private lateinit var currentUser: CustomerModel
    private val productsInShoppingCart = ArrayList<ProductRealmModel>()

    fun setCurrentUser(user: CustomerModel){
        currentUser = user
    }

    fun getCurrentUser(): CustomerRealmModel? {
        return Realm.getDefaultInstance()
            .where(CustomerRealmModel::class.java)
            .findFirst()
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