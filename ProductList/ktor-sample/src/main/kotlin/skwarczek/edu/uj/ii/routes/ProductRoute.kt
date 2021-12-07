package skwarczek.edu.uj.ii.routes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import skwarczek.edu.uj.ii.data.model.Product

private const val BASE_URL = "http://192.168.13.243:8080"

private val products = listOf(
    Product("Banana", "Sweet and yummy", "$BASE_URL/products/banana.jpg"),
    Product("Apple", "Sour and cool", "$BASE_URL/products/apple.jpg"),
    Product("Carrot", "Orange and savoury", "$BASE_URL/products/carrot.jpg"),
)

fun Route.getProducts(){
    get("/product"){
        call.respond(
            HttpStatusCode.OK,
            products.random()
        )
    }
}