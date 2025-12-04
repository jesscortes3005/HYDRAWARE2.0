package com.example.hydraware20

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.scale
import com.example.hydraware20.ui.theme.Hydraware20Theme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydraware20.viewModel.AuthViewModel
import com.example.hydraware20.viewModel.TanqueViewModel
import com.google.firebase.auth.FirebaseAuth

// Importar TanqueViewModelFactory desde HomeScreen
import com.example.hydraware20.TanqueViewModelFactory
import com.example.hydraware20.AnalisisTanqueScreen
import com.example.hydraware20.NotificacionesScreen
import com.example.hydraware20.YoScreen
import com.example.hydraware20.viewModel.NotificacionesViewModel

// Data class no cambia
data class User(
    val usuario: String,
    val password: String
)

// UserRepository no cambia
class UserRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUser(user: User): Boolean {
        val users = getUsers().toMutableList()

        if (users.any { it.usuario == user.usuario }) {
            return false
        }

        users.add(user)
        val usersJson = gson.toJson(users)
        sharedPreferences.edit()
            .putString("users", usersJson)
            .apply()
        return true
    }

    fun validateUser(usuario: String, password: String): Boolean {
        val users = getUsers()
        return users.any { it.usuario == usuario && it.password == password }
    }

    fun saveCurrentUser(usuario: String) {
        sharedPreferences.edit()
            .putString("current_user", usuario)
            .apply()
    }

    fun getCurrentUser(): String? {
        return sharedPreferences.getString("current_user", null)
    }

    fun clearCurrentUser() {
        sharedPreferences.edit()
            .remove("current_user")
            .apply()
    }

    private fun getUsers(): List<User> {
        val usersJson = sharedPreferences.getString("users", "[]")
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(usersJson, type) ?: emptyList()
    }
}

// MainActivity no cambia
class MainActivity : ComponentActivity() {
    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hydraware20Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("login") }
    var currentUser by remember { mutableStateOf("") }
    var selectedTanqueId by remember { mutableStateOf<String?>(null) }
    
    // ViewModel compartido para toda la navegación
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val tanqueViewModel: TanqueViewModel = viewModel(factory = TanqueViewModelFactory(context))

    // Verificar si hay un usuario logueado al iniciar la app
    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            currentUser = user.email?.substringBefore("@") ?: "Usuario"
            currentScreen = "home"
        } else {
            currentScreen = "login"
        }
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            // Transición de deslizamiento horizontal con fade
            (fadeIn(animationSpec = tween(300)) + 
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            )) togetherWith (fadeOut(animationSpec = tween(300)) + 
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            ))
        },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
        "login" -> {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    val auth = FirebaseAuth.getInstance()
                    currentUser = auth.currentUser?.email?.substringBefore("@") ?: "Usuario"
                    currentScreen = "home"
                },
                onNavigateToRegister = {
                    currentScreen = "register"
                }
            )
        }
        "register" -> {
            RegisterScreen(
                onNavigateToLogin = {
                    currentScreen = "login"
                },
                viewModel = authViewModel
            )
        }
        "home" -> {
            // Usar el mismo ViewModel para mantener sincronización
            HomeScreen(
                userName = currentUser.ifEmpty { "Usuario" },
                onLogoutClick = {
                    currentUser = ""
                    currentScreen = "login"
                },
                viewModel = authViewModel,
                tanqueViewModel = tanqueViewModel,
                onAddClick = {
                    currentScreen = "registrar_tanque"
                },
                onVerEstadoClick = { tanqueId ->
                    selectedTanqueId = tanqueId
                    currentScreen = "analisis_tanque"
                },
                onNotificationsClick = {
                    currentScreen = "yo"
                },
                currentScreen = currentScreen
            )
        }
        "yo" -> {
            val notificacionesViewModel = remember {
                NotificacionesViewModel(tanqueViewModel.tanques)
            }
            
            // Actualizar ViewModel cuando cambian los tanques
            LaunchedEffect(tanqueViewModel.tanques) {
                notificacionesViewModel.actualizarTanques(tanqueViewModel.tanques)
            }
            
            YoScreen(
                viewModel = notificacionesViewModel,
                onBackClick = {
                    currentScreen = "home"
                },
                onNotificationsClick = {
                    currentScreen = "notificaciones"
                },
                onAcercaDeClick = {
                    currentScreen = "acerca_de"
                },
                onInformacionPersonalClick = {
                    currentScreen = "informacion_personal"
                }
            )
        }
        "notificaciones" -> {
            val notificacionesViewModel = remember {
                NotificacionesViewModel(tanqueViewModel.tanques)
            }
            
            // Actualizar ViewModel cuando cambian los tanques
            LaunchedEffect(tanqueViewModel.tanques) {
                notificacionesViewModel.actualizarTanques(tanqueViewModel.tanques)
            }
            
            NotificacionesScreen(
                viewModel = notificacionesViewModel,
                onBackClick = {
                    currentScreen = "yo"
                }
            )
        }
        "registrar_tanque" -> {
            RegistrarTanqueScreen(
                viewModel = tanqueViewModel,
                onClose = {
                    currentScreen = "home"
                },
                onComplete = {
                    currentScreen = "home"
                }
            )
        }
        "analisis_tanque" -> {
            // Buscar el tanque de forma reactiva
            val tanque = remember(selectedTanqueId, tanqueViewModel.tanques) {
                selectedTanqueId?.let { id ->
                    tanqueViewModel.tanques.find { it.id == id }
                }
            }
            
            if (tanque != null) {
                AnalisisTanqueScreen(
                    tanque = tanque,
                    onBackClick = {
                        selectedTanqueId = null
                        currentScreen = "home"
                    }
                )
            } else {
                // Si no se encuentra el tanque, volver a home después de un momento
                LaunchedEffect(selectedTanqueId) {
                    if (selectedTanqueId != null) {
                        delay(100)
                        selectedTanqueId = null
                        currentScreen = "home"
                    }
                }
                // Mostrar loading mientras se busca
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        }
    }
}