package com.example.hydraware20.viewModel


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // --- Estados que la UI observará ---

    // True si el login/registro fue exitoso (para navegar)
    var authSuccess by mutableStateOf(false)
        private set // Solo el ViewModel puede cambiar esto

    // Contiene el mensaje de error, si hay uno
    var authError by mutableStateOf<String?>(null)
        private set

    // True mientras Firebase está procesando
    var isLoading by mutableStateOf(false)
        private set

    // --- Funciones que la UI llamará ---

    fun iniciarSesion(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            authError = "El email y la contraseña no pueden estar vacíos."
            return
        }

        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Login exitoso: ${auth.currentUser?.uid}")
                    authSuccess = true
                    authError = null
                } else {
                    Log.w("AuthViewModel", "Error en login", task.exception)
                    // El usuario de tu dialog es "Usuario no registrado"
                    // Firebase da errores más específicos, pero usaremos el tuyo.
                    authError = "Email o contraseña incorrectos. Por favor, verifique."
                }
                isLoading = false
            }
    }

    fun registrarUsuario(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            authError = "El email y la contraseña no pueden estar vacíos."
            return
        }

        isLoading = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Registro exitoso: ${auth.currentUser?.uid}")
                    authSuccess = true
                    authError = null
                } else {
                    Log.w("AuthViewModel", "Error en registro", task.exception)
                    authError = task.exception?.message ?: "No se pudo completar el registro."
                }
                isLoading = false
            }
    }

    // --- Funciones de control de estado ---

    /** Llama a esto para cerrar el diálogo de error */
    fun dismissError() {
        authError = null
    }

    /** Llama a esto después de navegar, para resetear el estado */
    fun resetAuthSuccess() {
        authSuccess = false
    }

    /** Función para cerrar sesión completamente */
    fun cerrarSesion() {
        auth.signOut()
        authSuccess = false
        authError = null
        isLoading = false
        Log.d("AuthViewModel", "Sesión cerrada exitosamente")
    }
}