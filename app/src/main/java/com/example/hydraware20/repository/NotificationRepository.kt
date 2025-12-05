package com.example.hydraware20.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.hydraware20.data.Notification
import com.example.hydraware20.data.NotificationType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Repositorio para gestionar las notificaciones almacenadas localmente
 */
class NotificationRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("notification_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val KEY_NOTIFICATIONS = "notifications"
    private val KEY_UNREAD_COUNT = "unread_count"

    /**
     * Guarda una notificación
     */
    fun saveNotification(notification: Notification): Boolean {
        val notifications = getNotifications().toMutableList()
        
        // Evitar duplicados basándose en ID
        if (notifications.any { it.id == notification.id }) {
            return false
        }
        
        // Agregar al inicio de la lista (más recientes primero)
        notifications.add(0, notification)
        
        // Limitar a las últimas 100 notificaciones para evitar acumulación
        val limitedNotifications = if (notifications.size > 100) {
            notifications.take(100)
        } else {
            notifications
        }
        
        val notificationsJson = gson.toJson(limitedNotifications)
        sharedPreferences.edit()
            .putString(KEY_NOTIFICATIONS, notificationsJson)
            .apply()
        
        updateUnreadCount()
        return true
    }

    /**
     * Obtiene todas las notificaciones
     */
    fun getNotifications(): List<Notification> {
        val notificationsJson = sharedPreferences.getString(KEY_NOTIFICATIONS, "[]")
        val type = object : TypeToken<List<Notification>>() {}.type
        return gson.fromJson(notificationsJson, type) ?: emptyList()
    }

    /**
     * Obtiene las notificaciones no leídas
     */
    fun getUnreadNotifications(): List<Notification> {
        return getNotifications().filter { !it.read }
    }

    /**
     * Marca una notificación como leída
     */
    fun markAsRead(notificationId: String): Boolean {
        val notifications = getNotifications().toMutableList()
        val notification = notifications.find { it.id == notificationId }
        
        if (notification != null) {
            val index = notifications.indexOf(notification)
            notifications[index] = notification.copy(read = true)
            val notificationsJson = gson.toJson(notifications)
            sharedPreferences.edit()
                .putString(KEY_NOTIFICATIONS, notificationsJson)
                .apply()
            updateUnreadCount()
            return true
        }
        return false
    }

    /**
     * Marca todas las notificaciones como leídas
     */
    fun markAllAsRead(): Boolean {
        val notifications = getNotifications().map { it.copy(read = true) }
        val notificationsJson = gson.toJson(notifications)
        sharedPreferences.edit()
            .putString(KEY_NOTIFICATIONS, notificationsJson)
            .apply()
        updateUnreadCount()
        return true
    }

    /**
     * Elimina una notificación
     */
    fun deleteNotification(notificationId: String): Boolean {
        val notifications = getNotifications().toMutableList()
        val removed = notifications.removeAll { it.id == notificationId }
        if (removed) {
            val notificationsJson = gson.toJson(notifications)
            sharedPreferences.edit()
                .putString(KEY_NOTIFICATIONS, notificationsJson)
                .apply()
            updateUnreadCount()
        }
        return removed
    }

    /**
     * Elimina todas las notificaciones
     */
    fun deleteAllNotifications(): Boolean {
        sharedPreferences.edit()
            .putString(KEY_NOTIFICATIONS, "[]")
            .remove(KEY_UNREAD_COUNT)
            .apply()
        return true
    }

    /**
     * Obtiene el contador de notificaciones no leídas
     */
    fun getUnreadCount(): Int {
        return getUnreadNotifications().size
    }

    /**
     * Actualiza el contador de notificaciones no leídas en SharedPreferences
     */
    private fun updateUnreadCount() {
        val count = getUnreadCount()
        sharedPreferences.edit()
            .putInt(KEY_UNREAD_COUNT, count)
            .apply()
    }
}
