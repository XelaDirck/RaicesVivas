package com.example.raicesvivas

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

object Usuarios : Table("usuarios") {
    val id = integer("id").autoIncrement()
    val nombreCompleto = varchar("nombre_completo", 100)
    val nombreUsuario = varchar("nombre_usuario", 50).uniqueIndex()
    val correo = varchar("correo", 100).uniqueIndex()
    val contrasena = varchar("contrasena", 255)
    val edad = integer("edad")
    val pais = varchar("pais", 50)
    val fotoPerfil = varchar("foto_perfil", 500).nullable()
    val fechaRegistro = varchar("fecha_registro", 50)
    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class RegistroRequest(
    val nombreCompleto: String,
    val nombreUsuario: String,
    val correo: String,
    val contrasena: String,
    val edad: Int,
    val pais: String,
    val fotoPerfil: String? = null
)

@Serializable
data class LoginRequest(
    val correo: String,
    val contrasena: String
)

@Serializable
data class ApiResponse(val status: String, val mensaje: String)

fun initDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://:/?useSSL=false&allowPublicKeyRetrieval=true"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = System.getenv("MYSQLUSER")
        password = System.getenv("MYSQLPASSWORD")
        maximumPoolSize = 5
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
    transaction {
        SchemaUtils.createMissingTablesAndColumns(Usuarios)
    }
}

fun main() {
    initDatabase()
    embeddedServer(Netty, port =
plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    kotlin("plugin.serialization") version "1.9.23"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.example.raicesvivas.ApplicationKt")
}

dependencies {
    implementation("io.ktor:ktor-server-netty:2.3.10")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.jetbrains.exposed:exposed-core:0.44.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.44.1")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.mindrot:jbcrypt:0.4")
} = @"
package com.example.raicesvivas

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

object Usuarios : Table("usuarios") {
    val id = integer("id").autoIncrement()
    val nombreCompleto = varchar("nombre_completo", 100)
    val nombreUsuario = varchar("nombre_usuario", 50).uniqueIndex()
    val correo = varchar("correo", 100).uniqueIndex()
    val contrasena = varchar("contrasena", 255)
    val edad = integer("edad")
    val pais = varchar("pais", 50)
    val fotoPerfil = varchar("foto_perfil", 500).nullable()
    val fechaRegistro = varchar("fecha_registro", 50)
    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class RegistroRequest(
    val nombreCompleto: String,
    val nombreUsuario: String,
    val correo: String,
    val contrasena: String,
    val edad: Int,
    val pais: String,
    val fotoPerfil: String? = null
)

@Serializable
data class LoginRequest(
    val correo: String,
    val contrasena: String
)

@Serializable
data class ApiResponse(val status: String, val mensaje: String)

fun initDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://:/?useSSL=false&allowPublicKeyRetrieval=true"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = System.getenv("MYSQLUSER")
        password = System.getenv("MYSQLPASSWORD")
        maximumPoolSize = 5
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
    transaction {
        SchemaUtils.createMissingTablesAndColumns(Usuarios)
    }
}

fun main() {
    initDatabase()
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080) {
        install(ContentNegotiation) { json() }
        routing {
            get("/") {
                call.respond(ApiResponse("ok", "RaicesVivas API funcionando"))
            }
            post("/registro") {
                val req = call.receive<RegistroRequest>()
                val hash = BCrypt.hashpw(req.contrasena, BCrypt.gensalt())
                transaction {
                    Usuarios.insert {
                        it[nombreCompleto] = req.nombreCompleto
                        it[nombreUsuario] = req.nombreUsuario
                        it[correo] = req.correo
                        it[contrasena] = hash
                        it[edad] = req.edad
                        it[pais] = req.pais
                        it[fotoPerfil] = req.fotoPerfil
                        it[fechaRegistro] = java.time.LocalDateTime.now().toString()
                    }
                }
                call.respond(ApiResponse("ok", "Usuario registrado correctamente"))
            }
            post("/login") {
                val req = call.receive<LoginRequest>()
                val usuario = transaction {
                    Usuarios.select { Usuarios.correo eq req.correo }.singleOrNull()
                }
                if (usuario == null || !BCrypt.checkpw(req.contrasena, usuario[Usuarios.contrasena])) {
                    call.respond(ApiResponse("error", "Credenciales incorrectas"))
                } else {
                    call.respond(ApiResponse("ok", "Login exitoso. Bienvenido "))
                }
            }
        }
    }.start(wait = true)
}
