package com.example.hydraware20.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.hydraware20.Tank
import com.example.hydraware20.TankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TankViewModel(private val context: Context) : ViewModel() {
    private val repository = TankRepository(context)
    private var notificationViewModel: NotificationViewModel? = null
    
    private val _tanks = MutableStateFlow<List<Tank>>(emptyList())
    val tanks: StateFlow<List<Tank>> = _tanks.asStateFlow()
    
    init {
        loadTanks()
    }
    
    /**
     * Establece el NotificationViewModel para enviar notificaciones
     */
    fun setNotificationViewModel(notificationViewModel: NotificationViewModel) {
        this.notificationViewModel = notificationViewModel
    }
    
    fun loadTanks() {
        _tanks.value = repository.getTanks()
    }
    
    fun saveTank(tank: Tank): Boolean {
        val success = repository.saveTank(tank)
        if (success) {
            loadTanks()
            // Notificar la creación del estanque
            notificationViewModel?.notifyTankCreated(tank.name)
        }
        return success
    }
    
    fun deleteTank(tankId: String): Boolean {
        val success = repository.deleteTank(tankId)
        if (success) {
            loadTanks()
        }
        return success
    }
}

