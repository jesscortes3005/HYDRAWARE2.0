package com.example.hydraware20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hydraware20.ui.theme.Hydraware20Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hydraware20Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
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
    
    when (currentScreen) {
        "login" -> {
            LoginScreen(
                onLoginClick = { usuario, password ->
                    // Simular login exitoso
                    currentUser = usuario
                    currentScreen = "home"
                    println("Login successful: $usuario")
                },
                onRegistrarseClick = {
                    currentScreen = "register"
                }
            )
        }
        "register" -> {
            RegisterScreen(
                onRegisterClick = { usuario, password, confirmPassword ->
                    // Simular registro exitoso
                    currentUser = usuario
                    currentScreen = "home"
                    println("Register successful: $usuario")
                },
                onVolverLoginClick = {
                    currentScreen = "login"
                }
            )
        }
        "home" -> {
            HomeScreen(
                userName = currentUser,
                onLogoutClick = {
                    currentUser = ""
                    currentScreen = "login"
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(onLoginClick = { _, _ -> }, onRegistrarseClick = {})
}