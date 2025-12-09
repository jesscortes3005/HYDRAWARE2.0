package com.example.hydraware20

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import com.example.hydraware20.viewModel.NotificacionesViewModel
import com.example.hydraware20.viewModel.TipoNotificacion

@Composable
fun NotificacionesScreen(
    viewModel: NotificacionesViewModel,
    onBackClick: () -> Unit
) {
    val notificaciones by viewModel.notificaciones.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Status bar space
            Spacer(modifier = Modifier.height(40.dp))
            
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF666666)
                    )
                }
                
                Text(
                    text = "Notificaciones",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Notificaciones list
            if (notificaciones.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay notificaciones",
                        fontSize = 16.sp,
                        color = Color(0xFF999999)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    notificaciones.forEach { notificacion ->
                        NotificacionCard(notificacion = notificacion)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificacionCard(notificacion: com.example.hydraware20.viewModel.Notificacion) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5) // Light grey background
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header con iconos de advertencia
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Icono de advertencia izquierdo
                    Text(
                        text = "⚠",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    
                    Text(
                        text = when (notificacion.tipo) {
                            TipoNotificacion.ADVERTENCIA -> "ADVERTENCIA EN \"${notificacion.tanqueNombre}\""
                            TipoNotificacion.PELIGRO -> "PELIGRO EN \"${notificacion.tanqueNombre}\""
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    // Icono de advertencia derecho
                    Text(
                        text = "⚠",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Lista de problemas
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    notificacion.problemas.forEach { problema ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "•",
                                fontSize = 16.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = problema,
                                fontSize = 14.sp,
                                color = Color(0xFF333333)
                            )
                        }
                    }
                }
            }
        }
        
        // Sombra azul en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomCenter)
                .background(
                    color = Color(0xFF007AFF).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                )
        )
    }
}

