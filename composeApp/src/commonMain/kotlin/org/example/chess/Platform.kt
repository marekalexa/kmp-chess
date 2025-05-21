package org.example.chess

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform