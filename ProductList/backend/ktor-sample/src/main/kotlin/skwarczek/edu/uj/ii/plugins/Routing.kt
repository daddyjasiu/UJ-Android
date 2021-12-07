package skwarczek.edu.uj.ii.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import skwarczek.edu.uj.ii.routes.getProducts

fun Application.configureRouting() {

    routing {
        getProducts()
        // Static plugin. Try to access `/static/index.html`
        static {
            resources("static")
        }
    }
}
