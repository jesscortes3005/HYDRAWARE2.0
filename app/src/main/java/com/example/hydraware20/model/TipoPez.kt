package com.example.hydraware20.model

data class TipoPez(
    val nombre: String,
    val phMin: Float,
    val phMax: Float,
    val temperaturaMin: Float,
    val temperaturaMax: Float
)

object TiposPezRecomendados {
    val tipos = listOf(
        TipoPez("Tilapia", 6.5f, 8.5f, 25f, 32f),
        TipoPez("Bagre", 6.5f, 8.0f, 24f, 30f),
        TipoPez("Carpa", 6.5f, 9.0f, 20f, 30f),
        TipoPez("Trucha", 6.5f, 8.0f, 10f, 18f),
        TipoPez("Lobina", 6.5f, 8.5f, 20f, 28f),
        TipoPez("Camarón", 7.5f, 8.5f, 28f, 32f)
    )
}

