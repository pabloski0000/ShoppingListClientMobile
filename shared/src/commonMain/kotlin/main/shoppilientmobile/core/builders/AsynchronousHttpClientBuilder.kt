package main.shoppilientmobile.core.builders

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class AsynchronousHttpClientBuilder {
    private var engine: HttpClientEngine? = null
    private var jsonParser: HttpClientConfig<*>.() -> Unit = {}

    fun withMockEngine(httpClientEngine: HttpClientEngine): AsynchronousHttpClientBuilder {
        engine = httpClientEngine
        return this
    }

    fun addJsonParser(): AsynchronousHttpClientBuilder {
        jsonParser = {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        return this
    }

    fun build(): HttpClient {
        return if (engine != null) {
            HttpClient(engine = engine!!, jsonParser)
        } else {
            HttpClient(engineFactory = CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
                install(Logging) {
                    level = LogLevel.ALL
                }
            }
        }
    }
}