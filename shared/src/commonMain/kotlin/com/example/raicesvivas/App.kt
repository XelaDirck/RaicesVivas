package com.example.raicesvivas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import org.jetbrains.compose.resources.painterResource
import raicesvivas.shared.generated.resources.Res
import raicesvivas.shared.generated.resources.logo_raicesvivas

enum class Pantalla {
    SPLASH, ONBOARDING, LOGIN, REGISTRO, SELECCION_LENGUA
}

@Composable
fun App() {
    RaicesTheme {
        var pantalla by remember { mutableStateOf(Pantalla.SPLASH) }
        var onboardingVisto by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            pantalla = if (!onboardingVisto) Pantalla.ONBOARDING else Pantalla.LOGIN
        }
        when (pantalla) {
            Pantalla.SPLASH -> PantallaSplash()
            Pantalla.ONBOARDING -> PantallaOnboarding {
                onboardingVisto = true
                pantalla = Pantalla.LOGIN
            }
            Pantalla.LOGIN -> PantallaLogin(
                onCrearCuenta = { pantalla = Pantalla.REGISTRO },
                onEntrar = { pantalla = Pantalla.SELECCION_LENGUA },
                onSinCuenta = { pantalla = Pantalla.SELECCION_LENGUA }
            )
            Pantalla.REGISTRO -> PantallaRegistro(
                onRegistrado = { pantalla = Pantalla.SELECCION_LENGUA },
                onVolver = { pantalla = Pantalla.LOGIN }
            )
            Pantalla.SELECCION_LENGUA -> PantallaSeleccionLengua(
                onLenguaSeleccionada = { }
            )
        }
    }
}

@Composable
fun PantallaSplash() {
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(Res.drawable.logo_raicesvivas),
                contentDescription = "Logo RaicesVivas",
                modifier = Modifier.size(180.dp)
            )
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
fun PantallaLogin(onCrearCuenta: () -> Unit, onEntrar: () -> Unit, onSinCuenta: () -> Unit) {
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
            Spacer(Modifier.height(16.dp))
            Text("o continua sin cuenta", fontSize = 13.sp, color = GrisSuave)
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onSinCuenta, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = CafeTierra)) {
                Text("Entrar sin cuenta", fontSize = 16.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text("Algunas funciones estaran limitadas", fontSize = 12.sp, color = GrisSuave)
        }
    }
}

@Composable
fun PantallaRegistro(onRegistrado: () -> Unit, onVolver: () -> Unit) {
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

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(40.dp))
            Text("Crear cuenta", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(value = nombreCompleto, onValueChange = { nombreCompleto = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = nombreUsuario, onValueChange = { nombreUsuario = it }, label = { Text("Nombre de usuario") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo electronico") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contrasena") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
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
                            onRegistrado()
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
        }
    }
}

@Composable
fun PantallaSeleccionLengua(onLenguaSeleccionada: (String) -> Unit) {
    val lenguas = listOf(
        "Nahuatl" to "Mexicano central",
        "Maya" to "Peninsular",
        "Mixteco" to "Oaxaca",
        "Zapoteco" to "Valles centrales",
        "Otomi" to "Centro de Mexico",
        "Purepecha" to "Michoacan"
    )
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Spacer(Modifier.height(40.dp))
            Text("Elige tu lengua", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = CafeTierra, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text("Descarga el paquete para usarlo sin conexion.", fontSize = 14.sp, color = CafeTierra.copy(alpha = 0.7f), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))
            lenguas.forEach { (nombre, region) ->
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
            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = BeigeMaiz.copy(alpha = 0.5f))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("*", fontSize = 20.sp, color = Verde)
                    Spacer(Modifier.width(12.dp))
                    Text("Funciona completamente sin internet una vez descargado.", fontSize = 13.sp, color = CafeTierra)
                }
            }
        }
    }
}