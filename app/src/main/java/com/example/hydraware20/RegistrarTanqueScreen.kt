package com.example.hydraware20

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraware20.model.Tanque
import com.example.hydraware20.model.TipoPez
import com.example.hydraware20.model.TiposPezRecomendados
import com.example.hydraware20.viewModel.TanqueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarTanqueScreen(
    viewModel: TanqueViewModel,
    onClose: () -> Unit,
    onComplete: () -> Unit
) {
    var nombreTanque by remember { mutableStateOf("Tanque 1") }
    var definirPH by remember { mutableStateOf(false) }
    var definirTemperatura by remember { mutableStateOf(false) }
    var phMin by remember { mutableStateOf("") }
    var phMax by remember { mutableStateOf("") }
    var temperaturaMin by remember { mutableStateOf("") }
    var temperaturaMax by remember { mutableStateOf("") }
    var usarSugerencia by remember { mutableStateOf(false) }
    var tipoPezSeleccionado by remember { mutableStateOf<TipoPez?>(null) }
    var mostrarDialogoTipoPez by remember { mutableStateOf(false) }
    
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Close button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFFE5E5E5),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                // Title
                Text(
                    text = "Registrar Tanque",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                // Spacer to balance the close button
                Spacer(modifier = Modifier.size(40.dp))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Información de estanque section
            Text(
                text = "Información de estanque",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 4.dp, bottom = 16.dp)
            )
            
            // Ingresar Nombre
            Text(
                text = "Ingresar Nombre",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = nombreTanque,
                onValueChange = { nombreTanque = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD1D1D1),
                    unfocusedBorderColor = Color(0xFFD1D1D1),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )
            
            // Opción de sugerencia automática
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD) // Light blue
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { usarSugerencia = !usarSugerencia },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    width = 2.dp,
                                    color = if (usarSugerencia) Color(0xFF007AFF) else Color(0xFFD1D1D1),
                                    shape = CircleShape
                                )
                                .background(
                                    color = if (usarSugerencia) Color(0xFF007AFF) else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (usarSugerencia) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Usar sugerencia automática",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    if (usarSugerencia) {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Selector de tipo de pez
                        OutlinedButton(
                            onClick = { mostrarDialogoTipoPez = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF007AFF)
                            )
                        ) {
                            Text(
                                text = tipoPezSeleccionado?.nombre ?: "Seleccionar tipo de pez",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        if (tipoPezSeleccionado != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "pH: ${tipoPezSeleccionado!!.phMin} - ${tipoPezSeleccionado!!.phMax} | " +
                                        "Temp: ${tipoPezSeleccionado!!.temperaturaMin}°C - ${tipoPezSeleccionado!!.temperaturaMax}°C",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }
            
            // Definir pH checkbox
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (definirPH) Color(0xFFE3F2FD) else Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { definirPH = !definirPH },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    width = 2.dp,
                                    color = if (definirPH) Color(0xFF007AFF) else Color(0xFFD1D1D1),
                                    shape = CircleShape
                                )
                                .background(
                                    color = if (definirPH) Color(0xFF007AFF) else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (definirPH) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Definir pH",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    if (definirPH) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = phMin,
                                onValueChange = { phMin = it },
                                label = { Text("pH mínimo") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF007AFF),
                                    unfocusedBorderColor = Color(0xFFD1D1D1)
                                )
                            )
                            
                            OutlinedTextField(
                                value = phMax,
                                onValueChange = { phMax = it },
                                label = { Text("pH máximo") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF007AFF),
                                    unfocusedBorderColor = Color(0xFFD1D1D1)
                                )
                            )
                        }
                    }
                }
            }
            
            // Definir Temperatura checkbox
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (definirTemperatura) Color(0xFFE3F2FD) else Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { definirTemperatura = !definirTemperatura },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    width = 2.dp,
                                    color = if (definirTemperatura) Color(0xFF007AFF) else Color(0xFFD1D1D1),
                                    shape = CircleShape
                                )
                                .background(
                                    color = if (definirTemperatura) Color(0xFF007AFF) else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (definirTemperatura) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Definir Temperatura",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    if (definirTemperatura) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = temperaturaMin,
                                onValueChange = { temperaturaMin = it },
                                label = { Text("Temp. mínimo") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF007AFF),
                                    unfocusedBorderColor = Color(0xFFD1D1D1)
                                )
                            )
                            
                            OutlinedTextField(
                                value = temperaturaMax,
                                onValueChange = { temperaturaMax = it },
                                label = { Text("Temp. máximo") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF007AFF),
                                    unfocusedBorderColor = Color(0xFFD1D1D1)
                                )
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Complete button
            OutlinedButton(
                onClick = {
                    // Validar nombre
                    if (nombreTanque.isBlank()) {
                        return@OutlinedButton
                    }
                    
                    // Si usa sugerencia automática, aplicar valores del tipo de pez
                    val phMinValue = if (usarSugerencia && tipoPezSeleccionado != null) {
                        tipoPezSeleccionado!!.phMin
                    } else if (definirPH) {
                        phMin.toFloatOrNull() ?: 0f
                    } else {
                        null
                    }
                    
                    val phMaxValue = if (usarSugerencia && tipoPezSeleccionado != null) {
                        tipoPezSeleccionado!!.phMax
                    } else if (definirPH) {
                        phMax.toFloatOrNull() ?: 14f
                    } else {
                        null
                    }
                    
                    val tempMinValue = if (usarSugerencia && tipoPezSeleccionado != null) {
                        tipoPezSeleccionado!!.temperaturaMin
                    } else if (definirTemperatura) {
                        temperaturaMin.toFloatOrNull() ?: 0f
                    } else {
                        null
                    }
                    
                    val tempMaxValue = if (usarSugerencia && tipoPezSeleccionado != null) {
                        tipoPezSeleccionado!!.temperaturaMax
                    } else if (definirTemperatura) {
                        temperaturaMax.toFloatOrNull() ?: 50f
                    } else {
                        null
                    }
                    
                    // Validar que si está definido, tenga valores válidos
                    if (definirPH && (phMinValue == null || phMaxValue == null)) {
                        return@OutlinedButton
                    }
                    if (definirTemperatura && (tempMinValue == null || tempMaxValue == null)) {
                        return@OutlinedButton
                    }
                    
                    val nuevoTanque = Tanque(
                        nombre = nombreTanque.trim(),
                        definirPH = definirPH,
                        definirTemperatura = definirTemperatura,
                        phMin = phMinValue,
                        phMax = phMaxValue,
                        temperaturaMin = tempMinValue,
                        temperaturaMax = tempMaxValue
                    )
                    viewModel.agregarTanque(nuevoTanque)
                    onComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF007AFF)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 2.dp,
                    color = Color(0xFF007AFF)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF007AFF),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "COMPLETAR",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF007AFF)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    
    // Dialog para seleccionar tipo de pez
    if (mostrarDialogoTipoPez) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoTipoPez = false },
            title = {
                Text(
                    text = "Seleccionar tipo de pez",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TiposPezRecomendados.tipos.forEach { tipo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    tipoPezSeleccionado = tipo
                                    mostrarDialogoTipoPez = false
                                    // Auto-marcar las opciones cuando se selecciona un tipo
                                    definirPH = true
                                    definirTemperatura = true
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (tipoPezSeleccionado == tipo) 
                                    Color(0xFFE3F2FD) else Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = tipo.nombre,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "pH: ${tipo.phMin} - ${tipo.phMax} | " +
                                            "Temp: ${tipo.temperaturaMin}°C - ${tipo.temperaturaMax}°C",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoTipoPez = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

