package main.shoppilientmobile

import io.ktor.client.engine.darwin.Darwin

class HttpClientEngineFactoryBuilder {
    fun buildDarwinEngineFactory(): Darwin {
        return Darwin
    }
}