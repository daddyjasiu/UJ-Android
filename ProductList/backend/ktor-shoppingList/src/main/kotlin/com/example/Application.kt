package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.routes.configureRouting

fun main() {

    createDB()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()

    }.start(wait = true)
}
