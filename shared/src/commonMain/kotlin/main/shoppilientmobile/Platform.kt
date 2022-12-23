package main.shoppilientmobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform