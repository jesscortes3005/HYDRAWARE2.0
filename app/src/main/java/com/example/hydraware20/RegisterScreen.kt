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

@Composable
fun RegisterScreen(
    onRegisterClick: (usuario: String, password: String, confirmPassword: String) -> Unit,
    onVolverLoginClick: () -> Unit,
    showRegisterError: Boolean = false,
    onDismissRegisterError: () -> Unit = {},
    showRegisterSuccess: Boolean = false,
    onDismissRegisterSuccess: () -> Unit = {}
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    
    // Validación de campos
    val isFormValid = usuario.isNotBlank() && 
                     password.isNotBlank() && confirmPassword.isNotBlank() && 
                     acceptTerms && password == confirmPassword

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
            
            // Usuario field
            CustomTextField(
                value = usuario,
                onValueChange = { usuario = it },
                placeholder = "Usuario",
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
                        onRegisterClick(usuario.trim(), password.trim(), confirmPassword.trim())
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xFF007AFF) else Color(0xFFCCCCCC),
                    disabledContainerColor = Color(0xFFCCCCCC)
                )
            ) {
                Text(
                    text = "REGISTRARSE",
                    color = if (isFormValid) Color.White else Color(0xFF666666),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
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
                    onClick = onVolverLoginClick,
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
        
        // Error AlertDialog - User already exists
        if (showRegisterError) {
            AlertDialog(
                onDismissRequest = onDismissRegisterError,
                title = {
                    Text(
                        text = "Error de Registro",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                },
                text = {
                    Text(
                        text = "Este usuario ya existe. Por favor elija otro nombre de usuario.",
                        color = Color(0xFF333333)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = onDismissRegisterError,
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
        
        // Success AlertDialog - Registration successful
        if (showRegisterSuccess) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(
                        text = "Registro Exitoso",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007AFF)
                    )
                },
                text = {
                    Text(
                        text = "¡Usuario registrado exitosamente! Redirigiendo a la pantalla principal...",
                        color = Color(0xFF333333)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = onDismissRegisterSuccess,
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
        onRegisterClick = { _, _, _ -> },
        onVolverLoginClick = { },
        showRegisterError = false,
        onDismissRegisterError = { },
        showRegisterSuccess = false,
        onDismissRegisterSuccess = { }
    )
}