package pl.edu.uj.ii.skwarczek.productlist.data

import android.util.Log
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import pl.edu.uj.ii.skwarczek.productlist.services.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class ProductRealm : RealmObject() {
    @PrimaryKey
    var id : Int = -1
    var name : String = ""
    var price : Int = 0
    var description : String = ""
}

class Product {
    var id : Int = -1
    var name : String = ""
    var price : Int = -1
    var description : String = ""
}

fun mapProduct(productRealm: ProductRealm) : Product {
    return Product().apply {
        this.name = productRealm.name
        this.price = productRealm.price
        this.description = productRealm.description
        this.id = productRealm.id
    }
}

object Products {
    var products = mutableListOf<ProductRealm>()

    init {
        getProductsFromDB()
    }

    fun productDetails(id : Int) : String {
        val product = Realm.getDefaultInstance().where(ProductRealm::class.java)
            .equalTo("id", id)
            .findFirst()

        if(product == null)
            return "product with $id ID not found"

        return "Product: " + product.name + ", price: " + product.price
    }

    fun getProductsFromDB() {
        products = Realm.getDefaultInstance().where<ProductRealm>().findAll()
    }

    fun getProductsFromDbIntoList() : List<Product> {
        return products.map { mapProduct(it) }
//        return Realm.getDefaultInstance().where<ProductRealm>().findAll().map { mapProduct(it) }
    }

}

fun getProductsIntoDB() {
    val service = ProductService.create()
    val call = service.getProductsCall()
    call.enqueue(object : Callback<List<Product>> {
        override fun onResponse(
            call: Call<List<Product>>,
            response: Response<List<Product>>
        ) {
            if (response.code() == 200) {
                val productResponse = response.body()!!

                for(prod in productResponse) {
                    val tmpProduct = ProductRealm().apply {
                        this.id = prod.id
                        this.description = prod.description
                        this.name = prod.name
                        this.price = prod.price
                    }

                    Realm.getDefaultInstance().executeTransactionAsync {
                        it.insertOrUpdate(tmpProduct)
                    }
                }

                Log.d("GET_PRODUCTS_FROM_DB", "Products get successful")

            }
        }

        override fun onFailure(call: Call<List<Product>>, t: Throwable) {
            Log.d("GET_PRODUCTS_FROM_DB", t.message.toString())
        }

    })
}