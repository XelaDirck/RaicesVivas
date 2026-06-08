package com.example.raicesvivas

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RegistroRequestDto(
    val nombreCompleto: String,
    val nombreUsuario: String,
    val correo: String,
    val contrasena: String,
    val edad: Int,
    val pais: String
)

class RegistroRepository {
    suspend fun getRegistro(): String {
        return httpClient.get("${ApiConfig.BASE_URL}/registro").bodyAsText()
    }

    suspend fun registrar(
        nombreCompleto: String,
        nombreUsuario: String,
        correo: String,
        contrasena: String,
        edad: Int,
        pais: String
    ): String {
        val response = httpClient.post("${ApiConfig.BASE_URL}/registro") {
            contentType(ContentType.Application.Json)
            setBody(RegistroRequestDto(nombreCompleto, nombreUsuario, correo, contrasena, edad, pais))
        }
        return response.bodyAsText()
    }
}
