package com.example.raicesvivas

fun validarNombreCompleto(valor: String): String? {
    if (valor.isBlank()) return "El nombre es obligatorio"
    if (!valor.all { it.isLetter() || it.isWhitespace() }) return "Solo se permiten letras"
    if (valor.trim().length < 50) return "Minimo 50 caracteres (tienes ${valor.trim().length})"
    return null
}

fun validarNombreUsuario(valor: String): String? {
    if (valor.isBlank()) return "El nombre de usuario es obligatorio"
    if (valor.contains(" ")) return "No se permiten espacios"
    if (valor.length > 20) return "Maximo 20 caracteres"
    if (valor.length < 3) return "Minimo 3 caracteres"
    return null
}

fun validarCorreo(valor: String): String? {
    if (valor.isBlank()) return "El correo es obligatorio"
    if (!valor.endsWith("@gmail.com")) return "El correo debe ser @gmail.com"
    if (valor.length < 12) return "Correo invalido"
    return null
}

fun validarContrasena(valor: String): String? {
    if (valor.length < 8) return "Minimo 8 caracteres"
    if (!valor.any { it.isUpperCase() }) return "Debe tener al menos una mayuscula"
    if (!valor.any { it.isDigit() }) return "Debe tener al menos un numero"
    return null
}

fun validarEdad(valor: String): String? {
    val edad = valor.toIntOrNull() ?: return "La edad debe ser un numero"
    if (edad < 5 || edad > 100) return "Edad debe estar entre 5 y 100"
    return null
}

fun validarPais(valor: String): String? {
    val paisesValidos = listOf("Mexico", "Colombia", "Argentina")
    if (valor !in paisesValidos) return "Selecciona: Mexico, Colombia o Argentina"
    return null
}

fun validarCorreoLogin(valor: String): String? {
    if (valor.isBlank()) return "El correo es obligatorio"
    if (!valor.endsWith("@gmail.com")) return "El correo debe ser @gmail.com"
    return null
}

fun validarContrasenaLogin(valor: String): String? {
    if (valor.isBlank()) return "La contrasena es obligatoria"
    if (valor.length < 8) return "Minimo 8 caracteres"
    return null
}