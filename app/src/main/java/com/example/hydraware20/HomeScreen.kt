package com.example.hydraware20

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import com.example.hydraware20.viewModel.AuthViewModel
import com.example.hydraware20.viewModel.TankViewModel
import com.example.hydraware20.viewModel.NotificationViewModel

@Composable
fun HomeScreen(
    userName: String,
    onLogoutClick: () -> Unit,
    viewModel: AuthViewModel,
    onAddTankClick: () -> Unit,
    tankViewModel: TankViewModel,
    onNotificationsClick: () -> Unit,
    notificationViewModel: NotificationViewModel
) {
    val tanks by tankViewModel.tanks.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()
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
            
            // Tanques list
            if (tanks.isEmpty()) {
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
                Text(
                    text = "Tanques registrados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tanks) { tank ->
                        TankCard(tank = tank)
                    }
                }
            }
            
            // Bottom navigation
            BottomNavigationBar(
                onHomeClick = { },
                onAddClick = onAddTankClick,
                onNotificationsClick = onNotificationsClick,
                unreadCount = unreadCount
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
    unreadCount: Int = 0
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
            
            Box {
                NavigationItem(
                    icon = Icons.Default.Notifications,
                    label = "Notificaciones",
                    onClick = onNotificationsClick
                )
                // Badge para mostrar notificaciones no leídas
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 12.dp, y = (-4).dp)
                            .background(
                                color = Color(0xFFF44336),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
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
fun TankCard(tank: Tank) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = tank.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            if (tank.phMin != null && tank.phMax != null) {
                Text(
                    text = "pH: ${tank.phMin} - ${tank.phMax}",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            
            if (tank.tempMin != null && tank.tempMax != null) {
                Text(
                    text = "Temperatura: ${tank.tempMin}°C - ${tank.tempMax}°C",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Para el preview, creamos un ViewModel mock
    // HomeScreen(
    //     userName = "Usuario de Prueba",
    //     onLogoutClick = { },
    //     viewModel = AuthViewModel(),
    //     onAddTankClick = { },
    //     tankViewModel = TankViewModel(context)
    // )
}
