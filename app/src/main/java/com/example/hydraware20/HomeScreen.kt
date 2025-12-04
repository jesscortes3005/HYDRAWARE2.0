package com.example.hydraware20

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.animation.core.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import com.example.hydraware20.model.Tanque
import com.example.hydraware20.viewModel.AuthViewModel
import com.example.hydraware20.viewModel.TanqueViewModel
import com.example.hydraware20.viewModel.TanqueStatusSimulatorViewModel

@Composable
fun HomeScreen(
    userName: String,
    onLogoutClick: () -> Unit,
    viewModel: AuthViewModel,
    tanqueViewModel: TanqueViewModel? = null,
    onAddClick: () -> Unit = {},
    onVerEstadoClick: (String) -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    currentScreen: String = "home"
) {
    val context = LocalContext.current
    val tanqueViewModelInstance: TanqueViewModel = tanqueViewModel ?: androidx.lifecycle.viewmodel.compose.viewModel(factory = TanqueViewModelFactory(context))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status bar space
            Spacer(modifier = Modifier.height(40.dp))
            
            // App title
            Text(
                text = "HYDRAWARE",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Welcome banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E40AF) // Dark blue
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "BIENVENIDO",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Tanques list - Scrollable content with LazyColumn for performance
            if (tanqueViewModelInstance.tanques.isEmpty()) {
                Text(
                    text = "No hay tanques registrados.",
                    fontSize = 16.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            } else {
                // Scrollable content area using LazyColumn for better performance
                // LazyColumn solo renderiza los items visibles, mejorando el rendimiento
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 100.dp // Espacio para la navegación inferior
                    )
                ) {
                    items(
                        items = tanqueViewModelInstance.tanques,
                        key = { tanque -> tanque.id }
                    ) { tanque ->
                        TanqueCard(
                            tanque = tanque,
                            onEditClick = { },
                            onDeleteClick = {
                                tanqueViewModelInstance.eliminarTanque(tanque.id)
                            },
                            onVerEstadoClick = { onVerEstadoClick(tanque.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
            
            // Bottom navigation - Fixed at bottom
            BottomNavigationBar(
                onHomeClick = { },
                onAddClick = onAddClick,
                onNotificationsClick = onNotificationsClick,
                currentScreen = currentScreen
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onAddClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    currentScreen: String = "home"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationItem(
                icon = Icons.Default.Home,
                label = "Home",
                onClick = onHomeClick,
                isSelected = currentScreen == "home"
            )
            
            NavigationItem(
                icon = Icons.Default.Add,
                label = "Agregar",
                onClick = onAddClick,
                isSelected = currentScreen == "registrar_tanque"
            )
            
            NavigationItem(
                icon = Icons.Default.Notifications,
                label = "Yo",
                onClick = onNotificationsClick,
                isSelected = currentScreen == "yo" || currentScreen == "notificaciones" || currentScreen == "acerca_de" || currentScreen == "informacion_personal"
            )
        }
    }
}

@Composable
fun NavigationItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    // Animación para el icono
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon_scale"
    )
    
    // Animación para el color
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF007AFF) else Color(0xFF999999),
        animationSpec = tween(300),
        label = "icon_color"
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF007AFF) else Color(0xFF999999),
        animationSpec = tween(300),
        label = "text_color"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier
                    .size(24.dp)
                    .scale(scale)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// Función para obtener el emoji del pez según el nombre del tanque
fun getPezEmoji(nombreTanque: String): String {
    val nombreLower = nombreTanque.lowercase()
    return when {
        nombreLower.contains("tilapia") -> "🐟"
        nombreLower.contains("bagre") -> "🐠"
        nombreLower.contains("carpa") -> "🐡"
        nombreLower.contains("trucha") -> "🐟"
        nombreLower.contains("lobina") -> "🐠"
        nombreLower.contains("camarón") || nombreLower.contains("camaron") -> "🦐"
        else -> "🐟" // Default
    }
}

@Composable
fun AnimatedFishIcon(nombreTanque: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "fish_animation")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset_x"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .offset(x = offsetX.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = getPezEmoji(nombreTanque),
            fontSize = 50.sp
        )
    }
}

@Composable
fun TanqueCard(
    tanque: Tanque,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onVerEstadoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ViewModel para simular el estado del tanque - usar remember para mantener la instancia
    // Solo crear ViewModel si el tanque tiene parámetros definidos para optimizar rendimiento
    val statusViewModel = remember(tanque.id, tanque.definirPH, tanque.definirTemperatura) {
        if (tanque.definirPH || tanque.definirTemperatura) {
            TanqueStatusSimulatorViewModel(tanque)
        } else {
            null
        }
    }
    
    // Observar los StateFlows del ViewModel de forma optimizada
    val phValue by if (statusViewModel != null && tanque.definirPH) {
        statusViewModel.phValue.collectAsState()
    } else {
        remember { mutableStateOf(0f) }
    }
    
    val tempValue by if (statusViewModel != null && tanque.definirTemperatura) {
        statusViewModel.tempValue.collectAsState()
    } else {
        remember { mutableStateOf(0f) }
    }
    
    val phEstado by if (statusViewModel != null && tanque.definirPH) {
        statusViewModel.phEstado.collectAsState()
    } else {
        remember { mutableStateOf(com.example.hydraware20.viewModel.EstadoSimulacion.NORMAL) }
    }
    
    val tempEstado by if (statusViewModel != null && tanque.definirTemperatura) {
        statusViewModel.tempEstado.collectAsState()
    } else {
        remember { mutableStateOf(com.example.hydraware20.viewModel.EstadoSimulacion.NORMAL) }
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Left side - Icon and name
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Fish icon - Square blue box with rounded corners
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = Color(0xFF007AFF), // Blue background
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Animated fish emoji
                        AnimatedFishIcon(nombreTanque = tanque.nombre)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = tanque.nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                
                // Right side - pH and Temperature (side by side)
                Row(
                    modifier = Modifier.weight(1.2f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    // pH Level
                    if (tanque.definirPH) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = "Nivel pH",
                                fontSize = 14.sp,
                                color = Color(0xFF999999),
                                fontWeight = FontWeight.Medium
                            )
                            // Mostrar valor simulado con color según estado
                            Text(
                                text = "${phValue.toInt()}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (statusViewModel != null) statusViewModel.getEstadoColor(phEstado) else Color(0xFF4CAF50),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                            Text(
                                text = if (statusViewModel != null) statusViewModel.getEstadoTexto(phEstado) else "Normal",
                                fontSize = 11.sp,
                                color = if (statusViewModel != null) statusViewModel.getEstadoColor(phEstado) else Color(0xFF4CAF50)
                            )
                        }
                    }
                    
                    // Temperature
                    if (tanque.definirTemperatura) {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Temperatura",
                                fontSize = 14.sp,
                                color = Color(0xFF999999),
                                fontWeight = FontWeight.Medium
                            )
                            // Mostrar valor simulado con color según estado
                            Text(
                                text = "${tempValue.toInt()}°C",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (statusViewModel != null) statusViewModel.getEstadoColor(tempEstado) else Color(0xFF4CAF50),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                            Text(
                                text = if (statusViewModel != null) statusViewModel.getEstadoTexto(tempEstado) else "Normal",
                                fontSize = 11.sp,
                                color = if (statusViewModel != null) statusViewModel.getEstadoColor(tempEstado) else Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
            
            // Edit and Delete buttons - Inside card, at the bottom right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .height(32.dp)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF)
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Editar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
                
                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Eliminar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
            
            // Ver Estado button - White with blue border
            OutlinedButton(
                onClick = onVerEstadoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF007AFF)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.5.dp,
                    color = Color(0xFF007AFF)
                )
            ) {
                Text(
                    text = "Ver Estado",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF007AFF)
                )
            }
        }
    }
}

// Factory for TanqueViewModel
class TanqueViewModelFactory(private val context: android.content.Context) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TanqueViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TanqueViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Para el preview, creamos un ViewModel mock
    HomeScreen(
        userName = "Usuario de Prueba",
        onLogoutClick = { },
        viewModel = AuthViewModel()
    )
}
