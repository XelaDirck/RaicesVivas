package com.example.raicesvivas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

enum class Pantalla {
    SPLASH, ONBOARDING, LOGIN, ENTRAR, REGISTRO, SELECCION_LENGUA, HOME
}

@Composable
fun App() {
    RaicesTheme {
        var pantalla by remember { mutableStateOf(Pantalla.SPLASH) }
        var onboardingVisto by remember { mutableStateOf(false) }
        var sesionActiva by remember { mutableStateOf(false) }
        var nombreUsuario by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            pantalla = when {
                sesionActiva -> Pantalla.HOME
                !onboardingVisto -> Pantalla.ONBOARDING
                else -> Pantalla.LOGIN
            }
        }

        when (pantalla) {
            Pantalla.SPLASH -> PantallaSplash()
            Pantalla.ONBOARDING -> PantallaOnboarding {
                onboardingVisto = true
                pantalla = Pantalla.LOGIN
            }
            Pantalla.LOGIN -> PantallaLogin(
                onCrearCuenta = { pantalla = Pantalla.REGISTRO },
                onEntrar = { pantalla = Pantalla.ENTRAR }
            )
            Pantalla.ENTRAR -> PantallaEntrar(
                onLoginExitoso = { nombre ->
                    nombreUsuario = nombre
                    sesionActiva = true
                    pantalla = Pantalla.HOME
                },
                onVolver = { pantalla = Pantalla.LOGIN }
            )
            Pantalla.REGISTRO -> PantallaRegistro(
                onRegistrado = { nombre ->
                    nombreUsuario = nombre
                    sesionActiva = true
                    pantalla = Pantalla.HOME
                },
                onVolver = { pantalla = Pantalla.LOGIN }
            )
            Pantalla.SELECCION_LENGUA -> PantallaSeleccionLengua(
                onLenguaSeleccionada = { pantalla = Pantalla.HOME },
                onVolver = { pantalla = Pantalla.HOME }
            )
            Pantalla.HOME -> PantallaHome(
                nombreUsuario = nombreUsuario,
                onSeleccionLengua = { pantalla = Pantalla.SELECCION_LENGUA }
            )
        }
    }
}

@Composable
fun TopBarConRegreso(titulo: String, onVolver: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 4.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onVolver) {
            Text("<", color = CafeTierra, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Text(titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
    }
}

@Composable
fun PantallaSplash() {
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Raices Vivas", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
            Spacer(Modifier.height(8.dp))
            Text("Preservando voces,\nconectando generaciones.", fontSize = 14.sp, color = Turquesa, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Row {
                Box(Modifier.size(8.dp).background(Terracota, RoundedCornerShape(4.dp)))
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(8.dp).background(Verde, RoundedCornerShape(4.dp)))
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(8.dp).background(Terracota, RoundedCornerShape(4.dp)))
            }
        }
    }
}

@Composable
fun PantallaOnboarding(onContinuar: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        TextButton(onClick = onContinuar, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
            Text("Saltar", color = Turquesa)
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Tu lengua\nes tu historia", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTierra, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Text("Cada palabra guarda el conocimiento de nuestros ancestros y la sabiduria de nuestra gente.", fontSize = 16.sp, color = CafeTierra.copy(alpha = 0.7f), textAlign = TextAlign.Center)
            Spacer(Modifier.height(48.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                Box(Modifier.size(10.dp).background(Verde, RoundedCornerShape(5.dp)))
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(10.dp).background(GrisSuave, RoundedCornerShape(5.dp)))
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(10.dp).background(GrisSuave, RoundedCornerShape(5.dp)))
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onContinuar,
                colors = ButtonDefaults.buttonColors(containerColor = Verde),
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(56.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(">", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun PantallaLogin(onCrearCuenta: () -> Unit, onEntrar: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Raices Vivas", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
            Spacer(Modifier.height(8.dp))
            Text("Preservando voces,\nconectando generaciones.", fontSize = 13.sp, color = Turquesa, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Row {
                Box(Modifier.size(8.dp).background(Terracota, RoundedCornerShape(4.dp)))
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(8.dp).background(Verde, RoundedCornerShape(4.dp)))
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(8.dp).background(Terracota, RoundedCornerShape(4.dp)))
            }
            Spacer(Modifier.height(40.dp))
            Text("Inicia sesion o crea tu cuenta", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = CafeTierra)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onCrearCuenta, colors = ButtonDefaults.buttonColors(containerColor = Verde), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Crear cuenta", fontSize = 16.sp, color = Color.White)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onEntrar, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = CafeTierra)) {
                Text("Entrar", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PantallaEntrar(onLoginExitoso: (String) -> Unit, onVolver: () -> Unit) {
    val repo = remember { RegistroRepository() }
    val scope = rememberCoroutineScope()
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    BackHandler { onVolver() }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            TopBarConRegreso("Entrar", onVolver)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo electronico") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contrasena") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), visualTransformation = PasswordVisualTransformation())
            Spacer(Modifier.height(16.dp))
            if (mensaje.isNotEmpty()) Text(mensaje, color = Terracota, fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        cargando = true
                        mensaje = try {
                            val resultado = repo.login(correo, contrasena)
                            if (resultado.contains("exitoso", true)) {
                                val nombre = resultado.substringAfter("Bienvenido").trim().trimEnd('"', '}')
                                onLoginExitoso(nombre)
                                ""
                            } else resultado
                        } catch (e: Exception) { "Error: ${e.message}" }
                        cargando = false
                    }
                },
                enabled = !cargando,
                colors = ButtonDefaults.buttonColors(containerColor = Verde),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                if (cargando) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                else Text("Entrar", color = Color.White, fontSize = 16.sp)
            }
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onVolver) { Text("No tengo cuenta, crear una", color = Turquesa) }
        }
    }
}

@Composable
fun PantallaRegistro(onRegistrado: (String) -> Unit, onVolver: () -> Unit) {
    val repo = remember { RegistroRepository() }
    val scope = rememberCoroutineScope()
    var nombreCompleto by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    BackHandler { onVolver() }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                TopBarConRegreso("Crear cuenta", onVolver)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = nombreCompleto, onValueChange = { nombreCompleto = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = nombreUsuario, onValueChange = { nombreUsuario = it }, label = { Text("Nombre de usuario") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo electronico") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contrasena") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), visualTransformation = PasswordVisualTransformation())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = pais, onValueChange = { pais = it }, label = { Text("Pais") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(16.dp))
                if (mensaje.isNotEmpty()) Text(mensaje, color = if (mensaje.contains("error", true)) Terracota else Verde, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        scope.launch {
                            cargando = true
                            mensaje = try {
                                repo.registrar(nombreCompleto, nombreUsuario, correo, contrasena, edad.toIntOrNull() ?: 0, pais)
                                onRegistrado(nombreUsuario)
                                ""
                            } catch (e: Exception) { "Error: ${e.message}" }
                            cargando = false
                        }
                    },
                    enabled = !cargando,
                    colors = ButtonDefaults.buttonColors(containerColor = Verde),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    if (cargando) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    else Text("Crear cuenta", color = Color.White, fontSize = 16.sp)
                }
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onVolver) { Text("Ya tengo cuenta", color = Turquesa) }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun PantallaSeleccionLengua(onLenguaSeleccionada: (String) -> Unit, onVolver: () -> Unit) {
    val lenguas = listOf(
        "Nahuatl" to "Mexicano central",
        "Maya" to "Peninsular",
        "Mixteco" to "Oaxaca",
        "Zapoteco" to "Valles centrales",
        "Otomi" to "Centro de Mexico",
        "Purepecha" to "Michoacan"
    )

    BackHandler { onVolver() }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                TopBarConRegreso("Elige tu lengua", onVolver)
                Text("Descarga el paquete para usarlo sin conexion.", fontSize = 14.sp, color = CafeTierra.copy(alpha = 0.7f), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
            }
            items(lenguas) { (nombre, region) ->
                Card(
                    onClick = { onLenguaSeleccionada(nombre) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(40.dp).background(Verde.copy(alpha = 0.15f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                            Text("*", fontSize = 20.sp, color = Verde)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(nombre, fontWeight = FontWeight.SemiBold, color = CafeTierra, fontSize = 16.sp)
                            Text(region, color = GrisSuave, fontSize = 13.sp)
                        }
                        Text("v", color = Verde, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun PantallaHome(nombreUsuario: String, onSeleccionLengua: () -> Unit) {
    val opciones = listOf(
        Triple("Aprender", "Practica tu lengua", Verde),
        Triple("Grabar", "Comparte tu voz", Terracota),
        Triple("Diccionario", "Explora palabras", Turquesa),
        Triple("Comunidad", "Conecta y comparte", CafeTierra)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                listOf("Inicio", "Aprender", "Grabar", "Diccionario", "Perfil").forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = index == 0,
                        onClick = {},
                        icon = { Text(when(index) { 0 -> "O"; 1 -> "A"; 2 -> "G"; 3 -> "D"; else -> "P" }, fontSize = 16.sp) },
                        label = { Text(label, fontSize = 10.sp) }
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(BeigeCalido).padding(padding).padding(24.dp)) {
            item {
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Hola, $nombreUsuario!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                        Text("Que bueno verte de nuevo", fontSize = 14.sp, color = CafeTierra.copy(alpha = 0.6f))
                    }
                }
                Spacer(Modifier.height(20.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Verde)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Tu progreso de hoy", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("Racha diaria", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(Modifier.height(4.dp))
                            Text("7 dias", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(progress = { 0.65f }, modifier = Modifier.size(64.dp), color = Color.White, trackColor = Verde.copy(alpha = 0.3f), strokeWidth = 6.dp)
                            Text("65%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Que quieres hacer hoy?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = CafeTierra)
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    opciones.take(2).forEach { (titulo, sub, color) ->
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = color), onClick = {}) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(titulo, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                                Text(sub, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    opciones.drop(2).forEach { (titulo, sub, color) ->
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = color), onClick = {}) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(titulo, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                                Text(sub, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Palabra del dia", fontSize = 13.sp, color = GrisSuave)
                        Text("Tlalli", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                        Text("Tierra", fontSize = 16.sp, color = Turquesa)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Button(onClick = onSeleccionLengua, colors = ButtonDefaults.buttonColors(containerColor = Turquesa), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    Text("Cambiar lengua", color = Color.White, fontSize = 16.sp)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}