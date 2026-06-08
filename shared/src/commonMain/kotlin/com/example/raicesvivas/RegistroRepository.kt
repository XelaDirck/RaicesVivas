package com.example.raicesvivas

import io.ktor.client.request.*
import io.ktor.client.statement.*

class RegistroRepository {
    suspend fun getRegistro(): String {
        return httpClient
            .get("${ApiConfig.BASE_URL}/registro")
            .bodyAsText()
    }
}