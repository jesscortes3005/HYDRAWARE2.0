package com.example.hydraware20

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
    onVerEstadoClick: (String) -> Unit = {}
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
            
            // Logout button - positioned at top right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        viewModel.cerrarSesion()
                        onLogoutClick()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Cerrar Sesión",
                        tint = Color(0xFF666666),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            // App title
            Text(
                text = "HYDRAWARE",
                fontSize = 24.sp,
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
                    .height(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E40AF) // Dark blue
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "BIENVENIDO",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = userName,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Tanques list - Scrollable content
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
                // Scrollable content area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                tanqueViewModelInstance.tanques.forEach { tanque ->
                    TanqueCard(
                        tanque = tanque,
                        onEditClick = { },
                        onDeleteClick = {
                            tanqueViewModelInstance.eliminarTanque(tanque.id)
                        },
                        onVerEstadoClick = { onVerEstadoClick(tanque.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        context = context
                    )
                }
                }
            }
            
            // Bottom navigation - Fixed at bottom
            BottomNavigationBar(
                onHomeClick = { },
                onAddClick = onAddClick,
                onNotificationsClick = { }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onAddClick: () -> Unit,
    onNotificationsClick: () -> Unit
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
                isSelected = true
            )
            
            NavigationItem(
                icon = Icons.Default.Add,
                label = "Agregar",
                onClick = onAddClick
            )
            
            NavigationItem(
                icon = Icons.Default.Notifications,
                label = "Notificaciones",
                onClick = onNotificationsClick
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF007AFF) else Color(0xFF007AFF),
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF007AFF),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun TanqueCard(
    tanque: Tanque,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onVerEstadoClick: () -> Unit,
    modifier: Modifier = Modifier,
    context: android.content.Context? = null
) {
    // ViewModel para simular el estado del tanque - usar remember para mantener la instancia
    val statusViewModel = remember(tanque.id) {
        TanqueStatusSimulatorViewModel(tanque)
    }
    
    // Observar los StateFlows del ViewModel
    val phValue by statusViewModel.phValue.collectAsState()
    val tempValue by statusViewModel.tempValue.collectAsState()
    val phEstado by statusViewModel.phEstado.collectAsState()
    val tempEstado by statusViewModel.tempEstado.collectAsState()
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
                            .size(70.dp)
                            .background(
                                color = Color(0xFF007AFF), // Blue background
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Fish emoji with water waves
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🐟",
                                fontSize = 40.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
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
                                fontSize = 11.sp,
                                color = Color(0xFF999999)
                            )
                            // Mostrar valor simulado
                            Text(
                                text = "${phValue.toInt()}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                            Text(
                                text = statusViewModel.getEstadoTexto(phEstado),
                                fontSize = 11.sp,
                                color = statusViewModel.getEstadoColor(phEstado)
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
                                fontSize = 11.sp,
                                color = Color(0xFF999999)
                            )
                            // Mostrar valor simulado
                            Text(
                                text = "${tempValue.toInt()}°C",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                            Text(
                                text = statusViewModel.getEstadoTexto(tempEstado),
                                fontSize = 11.sp,
                                color = statusViewModel.getEstadoColor(tempEstado)
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
