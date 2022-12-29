package main.shoppilientmobile.core.builders

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class SerializableHttpClientBuilder {
    private var engine: HttpClientEngine? = null

    fun withEngine(httpClientEngine: HttpClientEngine): SerializableHttpClientBuilder {
        engine = httpClientEngine
        return this
    }

    fun build(): HttpClient {
        return if (engine != null) {
            HttpClient(engine!!) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
        } else {
            HttpClient() {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
        }
    }
}