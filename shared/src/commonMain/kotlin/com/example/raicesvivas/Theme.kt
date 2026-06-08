package com.example.raicesvivas

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Verde = Color(0xFF2EBB57)
val Turquesa = Color(0xFF2AABA1)
val Terracota = Color(0xFFC65D3B)
val BeigeCalido = Color(0xFFFFFDF7)
val BeigeMaiz = Color(0xFFF4E382)
val CafeTierra = Color(0xFF6B4F3A)
val GrisSuave = Color(0xFFD9D3C7)

val RaicesColorScheme = lightColorScheme(
    primary = Verde,
    secondary = Turquesa,
    tertiary = Terracota,
    background = BeigeCalido,
    surface = BeigeCalido,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = CafeTierra,
    onSurface = CafeTierra,
)

@Composable
fun RaicesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RaicesColorScheme,
        content = content
    )
}
