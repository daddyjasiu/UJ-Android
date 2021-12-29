package pl.edu.uj.ii.skwarczek.productlist.models

import java.util.*

data class ProductModel (
    var id : Int = getAutoId(),
    var name : String = "",
    var description : String = ""
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}

