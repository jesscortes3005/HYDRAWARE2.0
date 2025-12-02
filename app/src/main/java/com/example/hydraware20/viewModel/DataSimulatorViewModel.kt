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
import kotlin.random.Random
import kotlin.math.min
import kotlin.math.max

data class DataPoint(
    val value: Float,
    val timestamp: Long = System.currentTimeMillis()
)

enum class EstadoValor {
    MUY_BAJO,    // Azul
    ESTABLE,     // Verde
    ADVERTENCIA, // Naranja
    FUERA_LIMITES // Rojo
}

class DataSimulatorViewModel(private val tanque: Tanque) : ViewModel() {
    
    private val _phDataPoints = MutableStateFlow<List<DataPoint>>(emptyList())
    val phDataPoints: StateFlow<List<DataPoint>> = _phDataPoints.asStateFlow()
    
    private val _temperaturaDataPoints = MutableStateFlow<List<DataPoint>>(emptyList())
    val temperaturaDataPoints: StateFlow<List<DataPoint>> = _temperaturaDataPoints.asStateFlow()
    
    private val _currentPh = MutableStateFlow(0f)
    val currentPh: StateFlow<Float> = _currentPh.asStateFlow()
    
    private val _currentTemperatura = MutableStateFlow(0f)
    val currentTemperatura: StateFlow<Float> = _currentTemperatura.asStateFlow()
    
    private val _phEstado = MutableStateFlow(EstadoValor.ESTABLE)
    val phEstado: StateFlow<EstadoValor> = _phEstado.asStateFlow()
    
    private val _temperaturaEstado = MutableStateFlow(EstadoValor.ESTABLE)
    val temperaturaEstado: StateFlow<EstadoValor> = _temperaturaEstado.asStateFlow()
    
    private var isSimulating = false
    
    init {
        // Inicializar con valores basados en el tanque
        val phInicial = tanque.ph ?: tanque.phMin ?: 7f
        val tempInicial = tanque.temperatura ?: tanque.temperaturaMin ?: 25f
        
        _currentPh.value = phInicial
        _currentTemperatura.value = tempInicial
        
        // Generar datos iniciales
        generateInitialData()
        startSimulation()
    }
    
    private fun generateInitialData() {
        val phMin = tanque.phMin ?: 6f
        val phMax = tanque.phMax ?: 8f
        val tempMin = tanque.temperaturaMin ?: 20f
        val tempMax = tanque.temperaturaMax ?: 28f
        
        // Asegurar que min < max
        val safePhMin = minOf(phMin, phMax)
        val safePhMax = maxOf(phMin, phMax)
        val safeTempMin = minOf(tempMin, tempMax)
        val safeTempMax = maxOf(tempMin, tempMax)
        
        val initialPhData = mutableListOf<DataPoint>()
        val initialTempData = mutableListOf<DataPoint>()
        
        // Asegurar que hay un rango válido
        val phRange = (safePhMax - safePhMin).coerceAtLeast(0.1f)
        val tempRange = (safeTempMax - safeTempMin).coerceAtLeast(0.1f)
        
        for (i in 0 until 20) {
            val progress = if (19 > 0) i / 19f else 0f
            val phValue = safePhMin + phRange * progress + Random.nextFloat() * 1f - 0.5f
            val tempValue = safeTempMin + tempRange * progress + Random.nextFloat() * 2f - 1f
            
            initialPhData.add(DataPoint(phValue.coerceIn(0f, 14f)))
            initialTempData.add(DataPoint(tempValue.coerceIn(0f, 50f)))
        }
        
        _phDataPoints.value = initialPhData
        _temperaturaDataPoints.value = initialTempData
        
        // Proteger contra listas vacías
        if (initialPhData.isNotEmpty()) {
            _currentPh.value = initialPhData.last().value
        }
        if (initialTempData.isNotEmpty()) {
            _currentTemperatura.value = initialTempData.last().value
        }
        
        updateEstados()
    }
    
    private fun startSimulation() {
        if (isSimulating) return
        isSimulating = true
        
        viewModelScope.launch {
            while (isSimulating) {
                delay(2000) // Actualizar cada 2 segundos
                
                val phMin = tanque.phMin ?: 6f
                val phMax = tanque.phMax ?: 8f
                val tempMin = tanque.temperaturaMin ?: 20f
                val tempMax = tanque.temperaturaMax ?: 28f
                
                // Asegurar que min < max
                val safePhMin = minOf(phMin, phMax)
                val safePhMax = maxOf(phMin, phMax)
                val safeTempMin = minOf(tempMin, tempMax)
                val safeTempMax = maxOf(tempMin, tempMax)
                
                // Generar valores basados en los rangos del tanque con variación realista
                val phRango = safePhMax - safePhMin
                val tempRango = safeTempMax - safeTempMin
                
                // Generar nuevo valor de pH con variación dentro del rango del tanque
                val phCentro = (safePhMin + safePhMax) / 2f
                val phVariation = Random.nextFloat() * phRango * 0.3f - phRango * 0.15f
                val newPh = (phCentro + phVariation).coerceIn(
                    (safePhMin - phRango * 0.5f).coerceAtLeast(0f),
                    (safePhMax + phRango * 0.5f).coerceAtMost(14f)
                )
                
                // Generar nuevo valor de temperatura con variación dentro del rango del tanque
                val tempCentro = (safeTempMin + safeTempMax) / 2f
                val tempVariation = Random.nextFloat() * tempRango * 0.3f - tempRango * 0.15f
                val newTemp = (tempCentro + tempVariation).coerceIn(
                    (safeTempMin - tempRango * 0.5f).coerceAtLeast(0f),
                    (safeTempMax + tempRango * 0.5f).coerceAtMost(50f)
                )
                
                _currentPh.value = newPh
                _currentTemperatura.value = newTemp
                
                // Agregar nuevos puntos de datos
                _phDataPoints.value = (_phDataPoints.value + DataPoint(newPh)).takeLast(30)
                _temperaturaDataPoints.value = (_temperaturaDataPoints.value + DataPoint(newTemp)).takeLast(30)
                
                updateEstados()
            }
        }
    }
    
    private fun updateEstados() {
        val phMin = tanque.phMin ?: 6f
        val phMax = tanque.phMax ?: 8f
        val tempMin = tanque.temperaturaMin ?: 20f
        val tempMax = tanque.temperaturaMax ?: 28f
        
        // Asegurar que min < max
        val safePhMin = minOf(phMin, phMax)
        val safePhMax = maxOf(phMin, phMax)
        val safeTempMin = minOf(tempMin, tempMax)
        val safeTempMax = maxOf(tempMin, tempMax)
        
        val currentPhValue = _currentPh.value
        val currentTempValue = _currentTemperatura.value
        
        // Determinar estado de pH
        _phEstado.value = when {
            currentPhValue < safePhMin * 0.7f -> EstadoValor.MUY_BAJO
            currentPhValue >= safePhMin * 0.7f && currentPhValue < safePhMin -> EstadoValor.ADVERTENCIA
            currentPhValue >= safePhMin && currentPhValue <= safePhMax -> EstadoValor.ESTABLE
            currentPhValue > safePhMax && currentPhValue <= safePhMax * 1.3f -> EstadoValor.ADVERTENCIA
            else -> EstadoValor.FUERA_LIMITES
        }
        
        // Determinar estado de temperatura
        _temperaturaEstado.value = when {
            currentTempValue < safeTempMin * 0.8f -> EstadoValor.MUY_BAJO
            currentTempValue >= safeTempMin * 0.8f && currentTempValue < safeTempMin -> EstadoValor.ADVERTENCIA
            currentTempValue >= safeTempMin && currentTempValue <= safeTempMax -> EstadoValor.ESTABLE
            currentTempValue > safeTempMax && currentTempValue <= safeTempMax * 1.2f -> EstadoValor.ADVERTENCIA
            else -> EstadoValor.FUERA_LIMITES
        }
    }
    
    fun getEstadoColor(estado: EstadoValor): androidx.compose.ui.graphics.Color {
        return when (estado) {
            EstadoValor.MUY_BAJO -> androidx.compose.ui.graphics.Color(0xFF2196F3) // Azul
            EstadoValor.ESTABLE -> androidx.compose.ui.graphics.Color(0xFF4CAF50) // Verde
            EstadoValor.ADVERTENCIA -> androidx.compose.ui.graphics.Color(0xFFFF9800) // Naranja
            EstadoValor.FUERA_LIMITES -> androidx.compose.ui.graphics.Color(0xFFF44336) // Rojo
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        isSimulating = false
    }
}

