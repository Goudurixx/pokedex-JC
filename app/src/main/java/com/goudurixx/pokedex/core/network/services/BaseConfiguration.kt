package com.goudurixx.pokedex.core.network.services

import android.util.Log
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class HttpLogger : Logger {
    override fun log(message: String) {
        Log.d("HttpLogger", message)
    }
}

fun HttpClientConfig<*>.default() {
    install(HttpTimeout) {
        connectTimeoutMillis = 5000
        requestTimeoutMillis = 5000
        socketTimeoutMillis = 5000
    }

    install(Logging) {
        logger = HttpLogger()
        level = LogLevel.BODY
//     TODO   level = if (BuildConfig.DEBUG) LogLevel.BODY else LogLevel.NONE
    }

    install(HttpCookies)

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }
}