package com.example.hydraware20.viewModel

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

enum class EstadoSimulacion {
    BAJO,        // 2 minutos
    NORMAL,     // 3 minutos
    ADVERTENCIA, // 2 minutos
    PELIGRO     // 4 minutos
}

class TanqueStatusSimulatorViewModel(private val tanque: Tanque) : ViewModel() {
    
    private val _phValue = MutableStateFlow(0f)
    val phValue: StateFlow<Float> = _phValue.asStateFlow()
    
    private val _tempValue = MutableStateFlow(0f)
    val tempValue: StateFlow<Float> = _tempValue.asStateFlow()
    
    private val _phEstado = MutableStateFlow(EstadoSimulacion.NORMAL)
    val phEstado: StateFlow<EstadoSimulacion> = _phEstado.asStateFlow()
    
    private val _tempEstado = MutableStateFlow(EstadoSimulacion.NORMAL)
    val tempEstado: StateFlow<EstadoSimulacion> = _tempEstado.asStateFlow()
    
    private var isSimulating = false
    
    init {
        // Inicializar con valores basados en el tanque
        val phInicial = tanque.ph ?: tanque.phMin ?: 7f
        val tempInicial = tanque.temperatura ?: tanque.temperaturaMin ?: 25f
        
        _phValue.value = phInicial
        _tempValue.value = tempInicial
        _phEstado.value = EstadoSimulacion.NORMAL
        _tempEstado.value = EstadoSimulacion.NORMAL
        
        startSimulation()
    }
    
    private fun startSimulation() {
        if (isSimulating) return
        isSimulating = true
        
        viewModelScope.launch {
            var estadoActual = EstadoSimulacion.BAJO
            var tiempoEnEstado = 0L
            val duracionEstados = mapOf(
                EstadoSimulacion.BAJO to 120000L,      // 2 minutos
                EstadoSimulacion.NORMAL to 180000L,    // 3 minutos
                EstadoSimulacion.ADVERTENCIA to 120000L, // 2 minutos
                EstadoSimulacion.PELIGRO to 240000L    // 4 minutos
            )
            
            while (isSimulating) {
                val duracion = duracionEstados[estadoActual] ?: 120000L
                
                // Actualizar estado si ha pasado el tiempo
                if (tiempoEnEstado >= duracion) {
                    estadoActual = getSiguienteEstado(estadoActual)
                    tiempoEnEstado = 0L
                }
                
                // Generar valores basados en el estado actual y los rangos del tanque
                val phMin = tanque.phMin ?: 6f
                val phMax = tanque.phMax ?: 8f
                val tempMin = tanque.temperaturaMin ?: 20f
                val tempMax = tanque.temperaturaMax ?: 28f
                
                val safePhMin = minOf(phMin, phMax)
                val safePhMax = maxOf(phMin, phMax)
                val safeTempMin = minOf(tempMin, tempMax)
                val safeTempMax = maxOf(tempMin, tempMax)
                
                val phRango = safePhMax - safePhMin
                val tempRango = safeTempMax - safeTempMin
                
                // Generar valores según el estado
                when (estadoActual) {
                    EstadoSimulacion.BAJO -> {
                        // Valores por debajo del mínimo
                        _phValue.value = (safePhMin - phRango * 0.3f).coerceIn(0f, 14f)
                        _tempValue.value = (safeTempMin - tempRango * 0.3f).coerceIn(0f, 50f)
                    }
                    EstadoSimulacion.NORMAL -> {
                        // Valores dentro del rango normal
                        val phCentro = (safePhMin + safePhMax) / 2f
                        val tempCentro = (safeTempMin + safeTempMax) / 2f
                        _phValue.value = phCentro.coerceIn(safePhMin, safePhMax)
                        _tempValue.value = tempCentro.coerceIn(safeTempMin, safeTempMax)
                    }
                    EstadoSimulacion.ADVERTENCIA -> {
                        // Valores cerca de los límites
                        _phValue.value = (safePhMax - phRango * 0.1f).coerceIn(safePhMin, safePhMax)
                        _tempValue.value = (safeTempMax - tempRango * 0.1f).coerceIn(safeTempMin, safeTempMax)
                    }
                    EstadoSimulacion.PELIGRO -> {
                        // Valores fuera de los límites
                        _phValue.value = (safePhMax + phRango * 0.3f).coerceIn(0f, 14f)
                        _tempValue.value = (safeTempMax + tempRango * 0.3f).coerceIn(0f, 50f)
                    }
                }
                
                _phEstado.value = estadoActual
                _tempEstado.value = estadoActual
                
                delay(1000) // Actualizar cada segundo
                tiempoEnEstado += 1000
            }
        }
    }
    
    private fun getSiguienteEstado(estadoActual: EstadoSimulacion): EstadoSimulacion {
        return when (estadoActual) {
            EstadoSimulacion.BAJO -> EstadoSimulacion.NORMAL
            EstadoSimulacion.NORMAL -> EstadoSimulacion.ADVERTENCIA
            EstadoSimulacion.ADVERTENCIA -> EstadoSimulacion.PELIGRO
            EstadoSimulacion.PELIGRO -> EstadoSimulacion.BAJO
        }
    }
    
    fun getEstadoColor(estado: EstadoSimulacion): androidx.compose.ui.graphics.Color {
        return when (estado) {
            EstadoSimulacion.BAJO -> androidx.compose.ui.graphics.Color(0xFF2196F3) // Azul
            EstadoSimulacion.NORMAL -> androidx.compose.ui.graphics.Color(0xFF4CAF50) // Verde
            EstadoSimulacion.ADVERTENCIA -> androidx.compose.ui.graphics.Color(0xFFFF9800) // Naranja
            EstadoSimulacion.PELIGRO -> androidx.compose.ui.graphics.Color(0xFFF44336) // Rojo
        }
    }
    
    fun getEstadoTexto(estado: EstadoSimulacion): String {
        return when (estado) {
            EstadoSimulacion.BAJO -> "Bajo"
            EstadoSimulacion.NORMAL -> "Normal"
            EstadoSimulacion.ADVERTENCIA -> "Advertencia"
            EstadoSimulacion.PELIGRO -> "Peligro"
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        isSimulating = false
    }
}

