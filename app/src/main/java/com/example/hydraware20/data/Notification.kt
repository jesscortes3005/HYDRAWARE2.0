package com.example.hydraware20.data

import java.util.Date

/**
 * Modelo de datos para representar una notificación en la aplicación
 */
data class Notification(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Long = System.currentTimeMillis(),
    val read: Boolean = false,
    val actionData: String? = null // Para datos adicionales (ej: ID del estanque relacionado)
) {
    /**
     * Obtiene la fecha formateada
     */
    fun getFormattedDate(): String {
        val date = Date(timestamp)
        val now = Date()
        val diff = now.time - timestamp

        return when {
            diff < 60000 -> "Hace un momento" // Menos de 1 minuto
            diff < 3600000 -> "Hace ${diff / 60000} minutos" // Menos de 1 hora
            diff < 86400000 -> "Hace ${diff / 3600000} horas" // Menos de 1 día
            diff < 604800000 -> "Hace ${diff / 86400000} días" // Menos de 1 semana
            else -> java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(date)
        }
    }
}

/**
 * Tipos de notificaciones
 */
enum class NotificationType {
    TANK_CREATED,    // Estanque creado
    ALERT,          // Alerta del sistema
    SYSTEM,         // Notificación general del sistema
    REMOTE          // Notificación remota (Firebase)
}
