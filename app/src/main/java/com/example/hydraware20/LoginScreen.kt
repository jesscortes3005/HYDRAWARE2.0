package com.example.hydraware20

// Imports necesarios (algunos son nuevos)
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.rememberCoroutineScope
import com.example.hydraware20.viewModel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // --- Tus estados de UI (se mantienen igual) ---
    var usuario by remember { mutableStateOf("") } // Esto será el email
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isButtonPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // --- Leemos el estado DESDE el ViewModel ---
    val isLoading = viewModel.isLoading
    val authError = viewModel.authError

    // 3. Reaccionamos al éxito del login para navegar
    LaunchedEffect(key1 = viewModel.authSuccess) {
        if (viewModel.authSuccess) {
            onLoginSuccess() // ¡Navega a la pantalla principal!
            viewModel.resetAuthSuccess() // Resetea el estado
        }
    }

    // Validación de campos
    val isFormValid = usuario.isNotBlank() && password.isNotBlank()

    // --- Tu diseño (se mantiene 99% igual) ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Hydraware",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Iniciar Sesión",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Usuario (Email) field
            CustomTextField(
                value = usuario,
                onValueChange = { usuario = it },
                placeholder = "Email", // Cambiado a Email, ya que Firebase lo requiere
                leadingIcon = Icons.Default.Person
            )

            // Password field
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                showPassword = showPassword,
                onPasswordVisibilityToggle = { showPassword = !showPassword }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Registration prompt
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "No estas Registrado?",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center
                )
                TextButton(
                    // 4. CAMBIO: Llama al parámetro de navegación
                    onClick = onNavigateToRegister,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Registrarse",
                        fontSize = 12.sp,
                        color = Color(0xFF333333)
                    )
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    if (isFormValid) {
                        isButtonPressed = true
                        // 5. CAMBIO: Llama al ViewModel en lugar del lambda
                        viewModel.iniciarSesion(usuario.trim(), password.trim())

                        coroutineScope.launch {
                            delay(200)
                            isButtonPressed = false
                        }
                    }
                },
                // 6. CAMBIO: Deshabilitado si es válido O si está cargando
                enabled = isFormValid && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        isButtonPressed && isFormValid -> Color(0xFF0056CC) // Darker blue when pressed
                        isFormValid -> Color(0xFF007AFF) // Normal blue
                        else -> Color(0xFFCCCCCC) // Disabled gray
                    },
                    disabledContainerColor = Color(0xFFCCCCCC)
                )
            ) {
                // 7. CAMBIO: Muestra un indicador de carga si es necesario
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "COMPLETAR",
                        color = if (isFormValid) Color.White else Color(0xFF666666),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // 8. CAMBIO: El AlertDialog ahora es controlado por el ViewModel
        if (authError != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissError() }, // Llama al ViewModel para cerrar
                title = {
                    Text(
                        text = "Error de Inicio de Sesión",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                },
                text = {
                    Text(
                        // Muestra el error específico del ViewModel
                        text = authError,
                        color = Color(0xFF333333)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.dismissError() }, // Llama al ViewModel para cerrar
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007AFF)
                        )
                    ) {
                        Text(
                            text = "OK",
                            color = Color.White
                        )
                    }
                }
            )
        }
    }
}

// --- Tu Composable (sin cambios) ---
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF999999)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF999999)
            )
        },
        trailingIcon = if (isPassword && onPasswordVisibilityToggle != null) {
            {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color(0xFF999999)
                    )
                }
            }
        } else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email // Cambiado a Email
        ),
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFE0E0E0),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedTextColor = Color(0xFF333333),
            unfocusedTextColor = Color(0xFF333333),
            cursorColor = Color(0xFF007AFF),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}

// --- Tu Preview (modificada para la nueva firma) ---
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // La preview no puede crear un ViewModel real,
    // pero podemos ver el diseño estático.
    LoginScreen(
        onLoginSuccess = {},
        onNavigateToRegister = {}
    )
}