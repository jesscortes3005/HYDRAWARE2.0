package com.example.hydraware20.model

data class Tanque(
    val id: String = "",
    val nombre: String,
    val definirPH: Boolean = false,
    val definirTemperatura: Boolean = false,
    val phMin: Float? = null,
    val phMax: Float? = null,
    val temperaturaMin: Float? = null,
    val temperaturaMax: Float? = null,
    // Mantener compatibilidad con versiones anteriores
    val ph: Float? = null,
    val temperatura: Float? = null
)

