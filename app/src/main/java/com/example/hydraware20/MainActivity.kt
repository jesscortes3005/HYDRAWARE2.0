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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydraware20.ui.theme.Hydraware20Theme
import com.example.hydraware20.service.NotificationService
import com.example.hydraware20.viewModel.AuthViewModel
import com.example.hydraware20.viewModel.TankViewModel
import com.example.hydraware20.viewModel.NotificationViewModel

// Data class no cambia
data class User(
    val usuario: String,
    val password: String
)

// Data class para Tanques
data class Tank(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val phMin: Double? = null,
    val phMax: Double? = null,
    val tempMin: Double? = null,
    val tempMax: Double? = null
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

// TankRepository
class TankRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("tank_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTank(tank: Tank): Boolean {
        val tanks = getTanks().toMutableList()
        tanks.add(tank)
        val tanksJson = gson.toJson(tanks)
        sharedPreferences.edit()
            .putString("tanks", tanksJson)
            .apply()
        return true
    }

    fun getTanks(): List<Tank> {
        val tanksJson = sharedPreferences.getString("tanks", "[]")
        val type = object : TypeToken<List<Tank>>() {}.type
        return gson.fromJson(tanksJson, type) ?: emptyList()
    }

    fun deleteTank(tankId: String): Boolean {
        val tanks = getTanks().toMutableList()
        val removed = tanks.removeAll { it.id == tankId }
        if (removed) {
            val tanksJson = gson.toJson(tanks)
            sharedPreferences.edit()
                .putString("tanks", tanksJson)
                .apply()
        }
        return removed
    }
}

// MainActivity
class MainActivity : ComponentActivity() {
    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar el canal de notificaciones
        NotificationService.createNotificationChannel(this)
        
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
    val context = LocalContext.current
    
    // ViewModel compartido para toda la navegación
    val authViewModel: AuthViewModel = viewModel()
    val tankViewModel: TankViewModel = remember { TankViewModel(context.applicationContext) }
    val notificationViewModel: NotificationViewModel = remember { NotificationViewModel(context.applicationContext) }

    // Conectar el NotificationViewModel con el TankViewModel
    LaunchedEffect(Unit) {
        tankViewModel.setNotificationViewModel(notificationViewModel)
    }

    // Verificar si hay un usuario logueado al iniciar la app
    LaunchedEffect(Unit) {
        // Aquí podrías verificar si hay un usuario autenticado en Firebase
        // Por ahora, empezamos en la pantalla de login
        currentScreen = "login"
    }

    when (currentScreen) {
        "login" -> {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    currentScreen = "home"
                },
                onNavigateToRegister = {
                    currentScreen = "register"
                }
            )
        }
        "register" -> {
            RegisterScreen(
                onRegisterSuccess = {
                    currentScreen = "home"
                },
                onNavigateToLogin = {
                    currentScreen = "login"
                },
                viewModel = authViewModel
            )
        }
        "home" -> {
            HomeScreen(
                userName = currentUser,
                onLogoutClick = {
                    currentUser = ""
                    currentScreen = "login"
                },
                viewModel = authViewModel,
                onAddTankClick = { currentScreen = "tank_register" },
                tankViewModel = tankViewModel,
                onNotificationsClick = { currentScreen = "notifications" },
                notificationViewModel = notificationViewModel
            )
        }
        "tank_register" -> {
            TankRegisterScreen(
                onClose = { currentScreen = "home" },
                onComplete = { currentScreen = "home" },
                viewModel = tankViewModel
            )
        }
        "notifications" -> {
            NotificationScreen(
                onBackClick = { currentScreen = "home" },
                viewModel = notificationViewModel
            )
        }
    }
}


