package pl.edu.uj.ii.skwarczek.productlist.models

data class ProductModel (
    var id : Int,
    var name : String,
    var description : String,
    var price: Double
)
//{
//    companion object{
//        fun getAutoId(): Int{
//            val random = Random()
//            return random.nextInt(100)
//        }
//    }
//}

