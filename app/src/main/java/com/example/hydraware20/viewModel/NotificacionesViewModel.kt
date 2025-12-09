package com.example.hydraware20.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydraware20.model.Tanque
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.max

data class Notificacion(
    val id: String,
    val tanqueId: String,
    val tanqueNombre: String,
    val tipo: TipoNotificacion,
    val problemas: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TipoNotificacion {
    ADVERTENCIA,
    PELIGRO
}

class NotificacionesViewModel(
    private val tanques: List<Tanque>
) : ViewModel() {
    
    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones.asStateFlow()
    
    private val statusViewModels = mutableMapOf<String, TanqueStatusSimulatorViewModel>()
    private var isMonitoring = false
    
    init {
        // Inicializar ViewModels para cada tanque
        tanques.forEach { tanque ->
            statusViewModels[tanque.id] = TanqueStatusSimulatorViewModel(tanque)
        }
        startMonitoring()
    }
    
    private fun startMonitoring() {
        if (isMonitoring) return
        isMonitoring = true
        
        viewModelScope.launch {
            while (isMonitoring) {
                delay(5000) // Verificar cada 5 segundos para mejor rendimiento
                updateNotificaciones()
            }
        }
    }
    
    private fun updateNotificaciones() {
        val nuevasNotificaciones = mutableListOf<Notificacion>()
        
        tanques.forEach { tanque ->
            val statusViewModel = statusViewModels[tanque.id] ?: return@forEach
            
            val phEstado = statusViewModel.phEstado.value
            val tempEstado = statusViewModel.tempEstado.value
            val phValue = statusViewModel.phValue.value
            val tempValue = statusViewModel.tempValue.value
            
            val problemas = mutableListOf<String>()
            var tipoNotificacion = TipoNotificacion.ADVERTENCIA
            
            // Verificar pH
            if (tanque.definirPH) {
                when (phEstado) {
                    EstadoSimulacion.BAJO -> {
                        problemas.add("Niveles Bajos de Ph")
                        tipoNotificacion = TipoNotificacion.ADVERTENCIA
                    }
                    EstadoSimulacion.ADVERTENCIA -> {
                        problemas.add("Niveles Altos de Ph")
                        tipoNotificacion = TipoNotificacion.ADVERTENCIA
                    }
                    EstadoSimulacion.PELIGRO -> {
                        problemas.add("Niveles Críticos de Ph")
                        tipoNotificacion = TipoNotificacion.PELIGRO
                    }
                    EstadoSimulacion.NORMAL -> {
                        // No agregar problema si está normal
                    }
                }
            }
            
            // Verificar temperatura
            if (tanque.definirTemperatura) {
                when (tempEstado) {
                    EstadoSimulacion.BAJO -> {
                        problemas.add("Niveles Bajos de Temperatura")
                        tipoNotificacion = TipoNotificacion.ADVERTENCIA
                    }
                    EstadoSimulacion.ADVERTENCIA -> {
                        problemas.add("Niveles Altos de Temperatura")
                        tipoNotificacion = TipoNotificacion.ADVERTENCIA
                    }
                    EstadoSimulacion.PELIGRO -> {
                        problemas.add("Niveles Críticos de Temperatura")
                        tipoNotificacion = TipoNotificacion.PELIGRO
                    }
                    EstadoSimulacion.NORMAL -> {
                        // No agregar problema si está normal
                    }
                }
            }
            
            // Si hay problemas, crear notificación
            if (problemas.isNotEmpty()) {
                val notificacion = Notificacion(
                    id = "${tanque.id}_${System.currentTimeMillis()}",
                    tanqueId = tanque.id,
                    tanqueNombre = tanque.nombre,
                    tipo = tipoNotificacion,
                    problemas = problemas
                )
                nuevasNotificaciones.add(notificacion)
            }
        }
        
        // Actualizar notificaciones (mantener solo las más recientes)
        _notificaciones.value = nuevasNotificaciones.sortedByDescending { it.timestamp }
    }
    
    fun actualizarTanques(nuevosTanques: List<Tanque>) {
        // Agregar ViewModels para nuevos tanques
        nuevosTanques.forEach { tanque ->
            if (!statusViewModels.containsKey(tanque.id)) {
                statusViewModels[tanque.id] = TanqueStatusSimulatorViewModel(tanque)
            }
        }
        
        // Remover ViewModels de tanques eliminados
        val idsExistentes = nuevosTanques.map { it.id }.toSet()
        statusViewModels.keys.removeAll { !idsExistentes.contains(it) }
    }
    
    override fun onCleared() {
        super.onCleared()
        isMonitoring = false
        // Los ViewModels se limpian automáticamente por el sistema de Android
        statusViewModels.clear()
    }
}

