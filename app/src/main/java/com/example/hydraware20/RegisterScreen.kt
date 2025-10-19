package com.example.hydraware20

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydraware20.viewModel.AuthViewModel

@Composable
fun RegisterScreen(
    // Parámetros de navegación
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    // ViewModel para Firebase Auth
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var isButtonPressed by remember { mutableStateOf(false) }
    
    // Estados del ViewModel
    val isLoading = viewModel.isLoading
    val authError = viewModel.authError
    
    // Validación de campos
    val isFormValid = email.isNotBlank() && 
                     password.isNotBlank() && confirmPassword.isNotBlank() && 
                     acceptTerms && password == confirmPassword
    
    // Reaccionamos al éxito del registro para navegar
    LaunchedEffect(key1 = viewModel.authSuccess) {
        if (viewModel.authSuccess) {
            onRegisterSuccess() // Navega a la pantalla principal
            viewModel.resetAuthSuccess() // Resetea el estado
        }
    }
    
    // Resetear el estado del botón después de un delay
    LaunchedEffect(key1 = isButtonPressed) {
        if (isButtonPressed) {
            kotlinx.coroutines.delay(200)
            isButtonPressed = false
        }
    }

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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // App title
            Text(
                text = "Hydraware",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Register section title
            Text(
                text = "Registro",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF333333),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Email field
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                leadingIcon = Icons.Default.Email
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
            
            // Confirm Password field
            CustomTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirmar Contraseña",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                showPassword = showConfirmPassword,
                onPasswordVisibilityToggle = { showConfirmPassword = !showConfirmPassword }
            )
            
            // Accept terms checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = acceptTerms,
                    onCheckedChange = { acceptTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF007AFF),
                        uncheckedColor = Color(0xFFE0E0E0),
                        checkmarkColor = Color.White
                    )
                )
                Text(
                    text = "Acepto los términos y condiciones",
                    color = Color(0xFF333333),
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Register button
            Button(
                onClick = { 
                    if (isFormValid) {
                        isButtonPressed = true
                        // Llamar al ViewModel para registrar con Firebase
                        viewModel.registrarUsuario(email.trim(), password.trim())
                    }
                },
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
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "REGISTRARSE",
                        color = if (isFormValid) Color.White else Color(0xFF666666),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Back to login link
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    fontSize = 14.sp,
                    color = Color(0xFF333333)
                )
                TextButton(
                    onClick = onNavigateToLogin,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        fontSize = 12.sp,
                        color = Color(0xFF333333)
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
        
        // Error AlertDialog - Firebase registration error
        if (authError != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissError() },
                title = {
                    Text(
                        text = "Error de Registro",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                },
                text = {
                    Text(
                        text = authError,
                        color = Color(0xFF333333)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.dismissError() },
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

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
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
            keyboardType = if (isPassword) KeyboardType.Password else keyboardType
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

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegisterSuccess = { },
        onNavigateToLogin = { }
    )
}