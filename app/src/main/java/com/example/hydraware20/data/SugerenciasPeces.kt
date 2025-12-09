package com.example.hydraware20.data

data class SugerenciaPeces(
    val tipo: String,
    val phMin: Float,
    val phMax: Float,
    val temperaturaMin: Float,
    val temperaturaMax: Float,
    val descripcion: String
)

object SugerenciasPecesRepository {
    val sugerencias = listOf(
        SugerenciaPeces(
            tipo = "Peces Tropicales Comunes",
            phMin = 6.5f,
            phMax = 7.5f,
            temperaturaMin = 24f,
            temperaturaMax = 28f,
            descripcion = "Guppies, Platies, Mollies, Tetras"
        ),
        SugerenciaPeces(
            tipo = "Peces de Agua Fría",
            phMin = 7.0f,
            phMax = 8.0f,
            temperaturaMin = 18f,
            temperaturaMax = 22f,
            descripcion = "Goldfish, Koi, Carpas"
        ),
        SugerenciaPeces(
            tipo = "Peces de Agua Ácida",
            phMin = 5.5f,
            phMax = 6.5f,
            temperaturaMin = 24f,
            temperaturaMax = 28f,
            descripcion = "Discos, Ángeles, Tetras del Amazonas"
        ),
        SugerenciaPeces(
            tipo = "Peces de Agua Alcalina",
            phMin = 7.5f,
            phMax = 8.5f,
            temperaturaMin = 24f,
            temperaturaMax = 28f,
            descripcion = "Cíclidos Africanos, Mollies"
        ),
        SugerenciaPeces(
            tipo = "Peces Betta",
            phMin = 6.0f,
            phMax = 7.5f,
            temperaturaMin = 24f,
            temperaturaMax = 28f,
            descripcion = "Betta Splendens"
        ),
        SugerenciaPeces(
            tipo = "Peces Marinos",
            phMin = 8.0f,
            phMax = 8.4f,
            temperaturaMin = 24f,
            temperaturaMax = 26f,
            descripcion = "Pez Payaso, Damiselas, Peces de Arrecife"
        )
    )
    
    fun obtenerSugerenciaPorTipo(tipo: String): SugerenciaPeces? {
        return sugerencias.find { it.tipo == tipo }
    }
}

