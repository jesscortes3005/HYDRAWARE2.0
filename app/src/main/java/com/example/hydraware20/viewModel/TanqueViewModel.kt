package com.example.hydraware20.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hydraware20.model.Tanque
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class TanqueViewModel(private val context: Context) : ViewModel() {
    
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("tanques_data", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    var tanques by mutableStateOf<List<Tanque>>(emptyList())
        private set
    
    init {
        cargarTanques()
    }
    
    fun agregarTanque(tanque: Tanque) {
        val nuevoTanque = tanque.copy(id = UUID.randomUUID().toString())
        val listaActualizada = tanques + nuevoTanque
        guardarTanques(listaActualizada)
        tanques = listaActualizada
    }
    
    fun eliminarTanque(id: String) {
        val listaActualizada = tanques.filter { it.id != id }
        guardarTanques(listaActualizada)
        tanques = listaActualizada
    }
    
    fun actualizarTanque(tanque: Tanque) {
        val listaActualizada = tanques.map { if (it.id == tanque.id) tanque else it }
        guardarTanques(listaActualizada)
        tanques = listaActualizada
    }
    
    private fun guardarTanques(lista: List<Tanque>) {
        val tanquesJson = gson.toJson(lista)
        sharedPreferences.edit()
            .putString("tanques", tanquesJson)
            .apply()
    }
    
    private fun cargarTanques() {
        val tanquesJson = sharedPreferences.getString("tanques", "[]")
        val type = object : TypeToken<List<Tanque>>() {}.type
        tanques = gson.fromJson(tanquesJson, type) ?: emptyList()
    }
}

