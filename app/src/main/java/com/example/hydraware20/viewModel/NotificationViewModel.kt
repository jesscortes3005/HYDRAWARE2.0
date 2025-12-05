package com.example.hydraware20.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hydraware20.data.Notification
import com.example.hydraware20.data.NotificationType
import com.example.hydraware20.repository.NotificationRepository
import com.example.hydraware20.service.NotificationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel(private val context: Context) : ViewModel() {
    private val repository = NotificationRepository(context)
    
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    init {
        loadNotifications()
    }
    
    /**
     * Carga las notificaciones desde el repositorio
     */
    fun loadNotifications() {
        _notifications.value = repository.getNotifications()
        _unreadCount.value = repository.getUnreadCount()
    }
    
    /**
     * Crea y muestra una notificación cuando se crea un estanque
     */
    fun notifyTankCreated(tankName: String) {
        val notification = Notification(
            title = "¡Estanque creado!",
            message = "Se ha registrado exitosamente el estanque: $tankName",
            type = NotificationType.TANK_CREATED,
            actionData = tankName
        )
        
        // Guardar en el repositorio
        repository.saveNotification(notification)
        
        // Mostrar notificación del sistema
        NotificationService.notifyTankCreated(context, tankName)
        
        // Actualizar la lista
        loadNotifications()
    }
    
    /**
     * Crea una notificación general del sistema
     */
    fun notifySystemEvent(title: String, message: String) {
        val notification = Notification(
            title = title,
            message = message,
            type = NotificationType.SYSTEM
        )
        
        repository.saveNotification(notification)
        NotificationService.notifySystemEvent(context, title, message)
        loadNotifications()
    }
    
    /**
     * Crea una alerta
     */
    fun notifyAlert(title: String, message: String) {
        val notification = Notification(
            title = title,
            message = message,
            type = NotificationType.ALERT
        )
        
        repository.saveNotification(notification)
        NotificationService.notifyAlert(context, title, message)
        loadNotifications()
    }
    
    /**
     * Procesa una notificación remota (desde Firebase, etc.)
     */
    fun processRemoteNotification(title: String, message: String, data: Map<String, String>? = null) {
        val notification = Notification(
            title = title,
            message = message,
            type = NotificationType.REMOTE,
            actionData = data?.get("action_data")
        )
        
        repository.saveNotification(notification)
        NotificationService.notifySystemEvent(context, title, message)
        loadNotifications()
    }
    
    /**
     * Marca una notificación como leída
     */
    fun markAsRead(notificationId: String) {
        repository.markAsRead(notificationId)
        loadNotifications()
    }
    
    /**
     * Marca todas las notificaciones como leídas
     */
    fun markAllAsRead() {
        repository.markAllAsRead()
        loadNotifications()
    }
    
    /**
     * Elimina una notificación
     */
    fun deleteNotification(notificationId: String) {
        repository.deleteNotification(notificationId)
        loadNotifications()
    }
    
    /**
     * Elimina todas las notificaciones
     */
    fun deleteAllNotifications() {
        repository.deleteAllNotifications()
        loadNotifications()
    }
}
