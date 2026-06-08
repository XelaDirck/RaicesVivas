package com.example.raicesvivas

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform