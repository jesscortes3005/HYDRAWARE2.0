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
        TipoPez("Guppy", 6.8f, 7.8f, 22f, 28f),
        TipoPez("Betta", 6.0f, 7.5f, 24f, 30f),
        TipoPez("Goldfish", 6.0f, 8.0f, 18f, 24f),
        TipoPez("Tetra", 6.0f, 7.5f, 22f, 28f),
        TipoPez("Cíclido", 7.0f, 8.5f, 24f, 28f),
        TipoPez("Molly", 7.0f, 8.5f, 22f, 28f),
        TipoPez("Platy", 7.0f, 8.5f, 20f, 26f),
        TipoPez("Angelfish", 6.0f, 7.5f, 24f, 30f),
        TipoPez("Discus", 5.5f, 7.0f, 26f, 30f),
        TipoPez("Neón", 5.5f, 7.0f, 20f, 26f),
        TipoPez("Genérico/Tropical", 6.5f, 7.5f, 24f, 28f)
    )
}

