package com.example.hydraware20.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hydraware20.MainActivity

object NotificationService {
    private const val CHANNEL_ID = "hydraware_notifications"
    private const val CHANNEL_NAME = "Notificaciones HYDRAWARE"
    private const val CHANNEL_DESCRIPTION = "Notificaciones sobre estanques y alertas del sistema"

    /**
     * Crea el canal de notificaciones. Debe llamarse al iniciar la aplicación.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Muestra una notificación local
     * @param context Contexto de la aplicación
     * @param title Título de la notificación
     * @param message Mensaje de la notificación
     * @param notificationId ID único para la notificación
     * @param priority Prioridad de la notificación (por defecto PRIORITY_HIGH)
     */
    fun showNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = System.currentTimeMillis().toInt(),
        priority: Int = NotificationCompat.PRIORITY_HIGH
    ) {
        // Intent para abrir la app cuando se toque la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .build()

        with(NotificationManagerCompat.from(context)) {
            // Verificar permisos antes de mostrar la notificación
            if (areNotificationsEnabled()) {
                notify(notificationId, notification)
            }
        }
    }

    /**
     * Notifica la creación de un nuevo estanque
     */
    fun notifyTankCreated(context: Context, tankName: String) {
        showNotification(
            context = context,
            title = "¡Estanque creado!",
            message = "Se ha registrado exitosamente el estanque: $tankName",
            notificationId = "tank_created_${System.currentTimeMillis()}".hashCode()
        )
    }

    /**
     * Notifica eventos generales del sistema
     */
    fun notifySystemEvent(context: Context, title: String, message: String) {
        showNotification(
            context = context,
            title = title,
            message = message,
            notificationId = "system_${System.currentTimeMillis()}".hashCode(),
            priority = NotificationCompat.PRIORITY_DEFAULT
        )
    }

    /**
     * Notifica alertas críticas
     */
    fun notifyAlert(context: Context, title: String, message: String) {
        showNotification(
            context = context,
            title = title,
            message = message,
            notificationId = "alert_${System.currentTimeMillis()}".hashCode(),
            priority = NotificationCompat.PRIORITY_MAX
        )
    }
}
