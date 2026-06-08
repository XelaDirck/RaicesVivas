package com.example.raicesvivas

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class RegistroRequestDto(
    val nombreCompleto: String,
    val nombreUsuario: String,
    val correo: String,
    val contrasena: String,
    val edad: Int,
    val pais: String
)

@Serializable
data class LoginRequestDto(
    val correo: String,
    val contrasena: String
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
        return httpClient.post("${ApiConfig.BASE_URL}/registro") {
            contentType(ContentType.Application.Json)
            setBody(RegistroRequestDto(nombreCompleto, nombreUsuario, correo, contrasena, edad, pais))
        }.bodyAsText()
    }

    suspend fun login(correo: String, contrasena: String): String {
        return httpClient.post("${ApiConfig.BASE_URL}/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(correo, contrasena))
        }.bodyAsText()
    }
}