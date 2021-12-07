package skwarczek.edu.uj.ii

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import skwarczek.edu.uj.ii.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}