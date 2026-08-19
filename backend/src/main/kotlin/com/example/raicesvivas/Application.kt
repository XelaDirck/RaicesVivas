package com.example.raicesvivas

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

// ==================== TABLAS ====================

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

object Lenguas : Table("lenguas") {
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 100)
    val region = varchar("region", 100)
    val descripcion = text("descripcion").nullable()
    val imagenUrl = varchar("imagen_url", 500).nullable()
    val activa = bool("activa").default(true)
    override val primaryKey = PrimaryKey(id)
}

object Niveles : Table("niveles") {
    val id = integer("id").autoIncrement()
    val lenguaId = integer("lengua_id")
    val nombre = varchar("nombre", 100)
    val descripcion = text("descripcion").nullable()
    val orden = integer("orden")
    override val primaryKey = PrimaryKey(id)
}

object Lecciones : Table("lecciones") {
    val id = integer("id").autoIncrement()
    val nivelId = integer("nivel_id")
    val titulo = varchar("titulo", 200)
    val descripcion = text("descripcion").nullable()
    val orden = integer("orden")
    override val primaryKey = PrimaryKey(id)
}

object Palabras : Table("palabras") {
    val id = integer("id").autoIncrement()
    val lenguaId = integer("lengua_id")
    val leccionId = integer("leccion_id").nullable()
    val palabraOriginal = varchar("palabra_original", 200)
    val traduccion = varchar("traduccion", 200)
    val pronunciacion = varchar("pronunciacion", 200).nullable()
    val imagenUrl = varchar("imagen_url", 500).nullable()
    val audioUrl = varchar("audio_url", 500).nullable()
    val ejemploUso = text("ejemplo_uso").nullable()
    val nivelDificultad = integer("nivel_dificultad").default(1)
    override val primaryKey = PrimaryKey(id)
}

object Ejercicios : Table("ejercicios") {
    val id = integer("id").autoIncrement()
    val leccionId = integer("leccion_id")
    val tipo = varchar("tipo", 50)
    val pregunta = text("pregunta")
    val audioPreguntaUrl = varchar("audio_pregunta_url", 500).nullable()
    val imagenUrl = varchar("imagen_url", 500).nullable()
    val orden = integer("orden")
    override val primaryKey = PrimaryKey(id)
}

object OpcionesRespuesta : Table("opciones_respuesta") {
    val id = integer("id").autoIncrement()
    val ejercicioId = integer("ejercicio_id")
    val texto = varchar("texto", 200)
    val esCorrecta = bool("es_correcta").default(false)
    val audioUrl = varchar("audio_url", 500).nullable()
    override val primaryKey = PrimaryKey(id)
}

object ProgresoUsuario : Table("progreso_usuario") {
    val id = integer("id").autoIncrement()
    val usuarioId = integer("usuario_id")
    val leccionId = integer("leccion_id")
    val completada = bool("completada").default(false)
    val puntuacion = integer("puntuacion").default(0)
    val intentos = integer("intentos").default(0)
    override val primaryKey = PrimaryKey(id)
}

object ProgresoLengua : Table("progreso_lengua") {
    val id = integer("id").autoIncrement()
    val usuarioId = integer("usuario_id")
    val lenguaId = integer("lengua_id")
    val descargada = bool("descargada").default(false)
    val rachaDias = integer("racha_dias").default(0)
    override val primaryKey = PrimaryKey(id)
}

object UbicacionesUsuario : Table("ubicaciones_usuario") {
    val id = integer("id").autoIncrement()
    val usuarioId = integer("usuario_id")
    val latitud = double("latitud")
    val longitud = double("longitud")
    val estado = varchar("estado", 100)
    val lenguaId = integer("lengua_id")
    val fecha = varchar("fecha", 50)
    override val primaryKey = PrimaryKey(id)
}

// ==================== MODELOS ====================

@Serializable data class RegistroRequest(val nombreCompleto: String, val nombreUsuario: String, val correo: String, val contrasena: String, val edad: Int, val pais: String, val fotoPerfil: String? = null)
@Serializable data class LoginRequest(val correo: String, val contrasena: String)
@Serializable data class ApiResponse(val status: String, val mensaje: String)

@Serializable data class LenguaDto(val id: Int, val nombre: String, val region: String, val descripcion: String?, val imagenUrl: String?, val activa: Boolean)
@Serializable data class NivelDto(val id: Int, val lenguaId: Int, val nombre: String, val descripcion: String?, val orden: Int)
@Serializable data class LeccionDto(val id: Int, val nivelId: Int, val titulo: String, val descripcion: String?, val orden: Int)
@Serializable data class PalabraDto(val id: Int, val lenguaId: Int, val leccionId: Int?, val palabraOriginal: String, val traduccion: String, val pronunciacion: String?, val imagenUrl: String?, val audioUrl: String?, val ejemploUso: String?, val nivelDificultad: Int)
@Serializable data class OpcionDto(val id: Int, val texto: String, val esCorrecta: Boolean, val audioUrl: String?)
@Serializable data class EjercicioDto(val id: Int, val leccionId: Int, val tipo: String, val pregunta: String, val audioPreguntaUrl: String?, val imagenUrl: String?, val orden: Int, val opciones: List<OpcionDto>)
@Serializable data class ProgresoRequest(val usuarioId: Int, val leccionId: Int, val puntuacion: Int)
@Serializable data class CrearPalabraRequest(val lenguaId: Int, val leccionId: Int?, val palabraOriginal: String, val traduccion: String, val pronunciacion: String?, val imagenUrl: String?, val audioUrl: String?, val ejemploUso: String?, val nivelDificultad: Int = 1)
@Serializable data class CrearEjercicioRequest(val leccionId: Int, val tipo: String, val pregunta: String, val audioPreguntaUrl: String?, val imagenUrl: String?, val orden: Int, val opciones: List<OpcionDto>)
@Serializable data class UbicacionRequest(val usuarioId: Int, val latitud: Double, val longitud: Double, val estado: String, val lenguaId: Int)
@Serializable data class ActualizarFotoRequest(val fotoUrl: String)

// ==================== DATABASE ====================

fun initDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://${System.getenv("MYSQLHOST")}:${System.getenv("MYSQLPORT")}/${System.getenv("MYSQLDATABASE")}?useSSL=false&allowPublicKeyRetrieval=true"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = System.getenv("MYSQLUSER")
        password = System.getenv("MYSQLPASSWORD")
        maximumPoolSize = 5
    }
    Database.connect(HikariDataSource(config))
    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            Usuarios, Lenguas, Niveles, Lecciones,
            Palabras, Ejercicios, OpcionesRespuesta,
            ProgresoUsuario, ProgresoLengua, UbicacionesUsuario
        )
    }
}

// ==================== MAIN ====================

fun main() {
    initDatabase()
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080) {
        install(ContentNegotiation) { json() }
        routing {

            // ---- SALUD ----
            get("/") { call.respond(ApiResponse("ok", "RaicesVivas API v2.0")) }

            // ---- AUTH ----
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
                val usuario = transaction { Usuarios.select { Usuarios.correo eq req.correo }.singleOrNull() }
                if (usuario == null || !BCrypt.checkpw(req.contrasena, usuario[Usuarios.contrasena])) {
                    call.respond(ApiResponse("error", "Correo o contrasena incorrectos"))
                } else {
                    call.respond(ApiResponse("ok", "Login exitoso. Bienvenido ${usuario[Usuarios.nombreUsuario]}|${usuario[Usuarios.id]}|${usuario[Usuarios.nombreCompleto]}"))
                }
            }

            put("/usuarios/{id}/foto") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respond(ApiResponse("error", "ID invalido"))
                val req = call.receive<ActualizarFotoRequest>()
                transaction {
                    Usuarios.update({ Usuarios.id eq id }) {
                        it[fotoPerfil] = req.fotoUrl
                    }
                }
                call.respond(ApiResponse("ok", "Foto actualizada correctamente"))
            }

            // ---- LENGUAS ----
            get("/lenguas") {
                val lista = transaction {
                    Lenguas.select { Lenguas.activa eq true }.map {
                        LenguaDto(it[Lenguas.id], it[Lenguas.nombre], it[Lenguas.region], it[Lenguas.descripcion], it[Lenguas.imagenUrl], it[Lenguas.activa])
                    }
                }
                call.respond(lista)
            }

            get("/lenguas/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(ApiResponse("error", "ID invalido"))
                val lengua = transaction { Lenguas.select { Lenguas.id eq id }.singleOrNull() }
                if (lengua == null) call.respond(ApiResponse("error", "Lengua no encontrada"))
                else call.respond(LenguaDto(lengua[Lenguas.id], lengua[Lenguas.nombre], lengua[Lenguas.region], lengua[Lenguas.descripcion], lengua[Lenguas.imagenUrl], lengua[Lenguas.activa]))
            }

            // ---- NIVELES ----
            get("/lenguas/{id}/niveles") {
                val lenguaId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(ApiResponse("error", "ID invalido"))
                val lista = transaction {
                    Niveles.select { Niveles.lenguaId eq lenguaId }.orderBy(Niveles.orden).map {
                        NivelDto(it[Niveles.id], it[Niveles.lenguaId], it[Niveles.nombre], it[Niveles.descripcion], it[Niveles.orden])
                    }
                }
                call.respond(lista)
            }

            // ---- LECCIONES ----
            get("/niveles/{id}/lecciones") {
                val nivelId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(ApiResponse("error", "ID invalido"))
                val lista = transaction {
                    Lecciones.select { Lecciones.nivelId eq nivelId }.orderBy(Lecciones.orden).map {
                        LeccionDto(it[Lecciones.id], it[Lecciones.nivelId], it[Lecciones.titulo], it[Lecciones.descripcion], it[Lecciones.orden])
                    }
                }
                call.respond(lista)
            }

            // ---- PALABRAS ----
            get("/lenguas/{id}/palabras") {
                val lenguaId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(ApiResponse("error", "ID invalido"))
                val lista = transaction {
                    Palabras.select { Palabras.lenguaId eq lenguaId }.map {
                        PalabraDto(it[Palabras.id], it[Palabras.lenguaId], it[Palabras.leccionId], it[Palabras.palabraOriginal], it[Palabras.traduccion], it[Palabras.pronunciacion], it[Palabras.imagenUrl], it[Palabras.audioUrl], it[Palabras.ejemploUso], it[Palabras.nivelDificultad])
                    }
                }
                call.respond(lista)
            }

            get("/lecciones/{id}/palabras") {
                val leccionId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(ApiResponse("error", "ID invalido"))
                val lista = transaction {
                    Palabras.select { Palabras.leccionId eq leccionId }.map {
                        PalabraDto(it[Palabras.id], it[Palabras.lenguaId], it[Palabras.leccionId], it[Palabras.palabraOriginal], it[Palabras.traduccion], it[Palabras.pronunciacion], it[Palabras.imagenUrl], it[Palabras.audioUrl], it[Palabras.ejemploUso], it[Palabras.nivelDificultad])
                    }
                }
                call.respond(lista)
            }

            post("/palabras") {
                val req = call.receive<CrearPalabraRequest>()
                transaction {
                    Palabras.insert {
                        it[lenguaId] = req.lenguaId
                        it[leccionId] = req.leccionId
                        it[palabraOriginal] = req.palabraOriginal
                        it[traduccion] = req.traduccion
                        it[pronunciacion] = req.pronunciacion
                        it[imagenUrl] = req.imagenUrl
                        it[audioUrl] = req.audioUrl
                        it[ejemploUso] = req.ejemploUso
                        it[nivelDificultad] = req.nivelDificultad
                    }
                }
                call.respond(ApiResponse("ok", "Palabra agregada correctamente"))
            }

            // ---- EJERCICIOS ----
            get("/lecciones/{id}/ejercicios") {
                val leccionId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(ApiResponse("error", "ID invalido"))
                val lista = transaction {
                    Ejercicios.select { Ejercicios.leccionId eq leccionId }.orderBy(Ejercicios.orden).map { ej ->
                        val opciones = OpcionesRespuesta.select { OpcionesRespuesta.ejercicioId eq ej[Ejercicios.id] }.map { op ->
                            OpcionDto(op[OpcionesRespuesta.id], op[OpcionesRespuesta.texto], op[OpcionesRespuesta.esCorrecta], op[OpcionesRespuesta.audioUrl])
                        }
                        EjercicioDto(ej[Ejercicios.id], ej[Ejercicios.leccionId], ej[Ejercicios.tipo], ej[Ejercicios.pregunta], ej[Ejercicios.audioPreguntaUrl], ej[Ejercicios.imagenUrl], ej[Ejercicios.orden], opciones)
                    }
                }
                call.respond(lista)
            }

            // ---- PROGRESO ----
            post("/progreso") {
                val req = call.receive<ProgresoRequest>()
                transaction {
                    val existente = ProgresoUsuario.select {
                        (ProgresoUsuario.usuarioId eq req.usuarioId) and (ProgresoUsuario.leccionId eq req.leccionId)
                    }.singleOrNull()
                    if (existente == null) {
                        ProgresoUsuario.insert {
                            it[usuarioId] = req.usuarioId
                            it[leccionId] = req.leccionId
                            it[puntuacion] = req.puntuacion
                            it[completada] = req.puntuacion >= 70
                            it[intentos] = 1
                        }
                    } else {
                        ProgresoUsuario.update({ (ProgresoUsuario.usuarioId eq req.usuarioId) and (ProgresoUsuario.leccionId eq req.leccionId) }) {
                            it[puntuacion] = req.puntuacion
                            it[completada] = req.puntuacion >= 70
                            it[intentos] = existente[ProgresoUsuario.intentos] + 1
                        }
                    }
                }
                call.respond(ApiResponse("ok", "Progreso guardado"))
            }

            get("/progreso/{usuarioId}") {
                val usuarioId = call.parameters["usuarioId"]?.toIntOrNull() ?: return@get call.respond(ApiResponse("error", "ID invalido"))
                val lista = transaction {
                    ProgresoUsuario.select { ProgresoUsuario.usuarioId eq usuarioId }.map {
                        mapOf(
                            "leccionId" to it[ProgresoUsuario.leccionId],
                            "completada" to it[ProgresoUsuario.completada],
                            "puntuacion" to it[ProgresoUsuario.puntuacion],
                            "intentos" to it[ProgresoUsuario.intentos]
                        )
                    }
                }
                call.respond(lista)
            }

            // ---- UBICACIONES (GPS) ----
            post("/ubicaciones") {
                val req = call.receive<UbicacionRequest>()
                transaction {
                    UbicacionesUsuario.insert {
                        it[usuarioId] = req.usuarioId
                        it[latitud] = req.latitud
                        it[longitud] = req.longitud
                        it[estado] = req.estado
                        it[lenguaId] = req.lenguaId
                        it[fecha] = java.time.LocalDateTime.now().toString()
                    }
                }
                call.respond(ApiResponse("ok", "Ubicacion registrada correctamente"))
            }

            // ---- DESCARGA COMPLETA POR LENGUA (para modo offline) ----
            get("/descarga/{lenguaId}") {
                val lenguaId = call.parameters["lenguaId"]?.toIntOrNull() ?: return@get call.respond(ApiResponse("error", "ID invalido"))
                val data = transaction {
                    val lengua = Lenguas.select { Lenguas.id eq lenguaId }.singleOrNull()
                    val niveles = Niveles.select { Niveles.lenguaId eq lenguaId }.orderBy(Niveles.orden).map {
                        NivelDto(it[Niveles.id], it[Niveles.lenguaId], it[Niveles.nombre], it[Niveles.descripcion], it[Niveles.orden])
                    }
                    val palabras = Palabras.select { Palabras.lenguaId eq lenguaId }.map {
                        PalabraDto(it[Palabras.id], it[Palabras.lenguaId], it[Palabras.leccionId], it[Palabras.palabraOriginal], it[Palabras.traduccion], it[Palabras.pronunciacion], it[Palabras.imagenUrl], it[Palabras.audioUrl], it[Palabras.ejemploUso], it[Palabras.nivelDificultad])
                    }
                    mapOf("lengua" to lengua?.get(Lenguas.nombre), "niveles" to niveles, "palabras" to palabras)
                }
                call.respond(data)
            }
        }
    }.start(wait = true)
}

