package com.example.hydraware20

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraware20.viewModel.TankViewModel

@Composable
fun TankRegisterScreen(
    onClose: () -> Unit,
    onComplete: () -> Unit,
    viewModel: TankViewModel
) {
    var tankName by remember { mutableStateOf("") }
    var enablePh by remember { mutableStateOf(false) }
    var phMin by remember { mutableStateOf("") }
    var phMax by remember { mutableStateOf("") }
    var enableTemp by remember { mutableStateOf(false) }
    var tempMin by remember { mutableStateOf("") }
    var tempMax by remember { mutableStateOf("") }

    var phError by remember { mutableStateOf<String?>(null) }
    var tempError by remember { mutableStateOf<String?>(null) }

    fun validatePh() {
        if (!enablePh) { phError = null; return }
        val min = phMin.toDoubleOrNull()
        val max = phMax.toDoubleOrNull()
        phError = when {
            phMin.isBlank() || phMax.isBlank() -> "Completa mínimo y máximo"
            min == null || max == null -> "Usa valores numéricos"
            min < 0 || max > 14 -> "Rango válido: 0 a 14"
            min >= max -> "El mínimo debe ser menor que el máximo"
            else -> null
        }
    }

    fun validateTemp() {
        if (!enableTemp) { tempError = null; return }
        val min = tempMin.toDoubleOrNull()
        val max = tempMax.toDoubleOrNull()
        tempError = when {
            tempMin.isBlank() || tempMax.isBlank() -> "Completa mínimo y máximo"
            min == null || max == null -> "Usa valores numéricos"
            min < -10 || max > 50 -> "Rango válido: -10 a 50 °C"
            min >= max -> "El mínimo debe ser menor que el máximo"
            else -> null
        }
    }

    LaunchedEffect(enablePh, phMin, phMax) { validatePh() }
    LaunchedEffect(enableTemp, tempMin, tempMax) { validateTemp() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterStart)
                        .background(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color(0xFF666666)
                    )
                }
                Text(
                    text = "Registrar Tanque",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Información de estanque",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4A4A4A)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresar Nombre",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF888888)
            )

            OutlinedTextField(
                value = tankName,
                onValueChange = { tankName = it },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                placeholder = { Text("Tanque 1") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Define pH card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SquareSwitch(
                        checked = enablePh,
                        onCheckedChange = { enablePh = it }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Definir pH",
                        color = Color(0xFF4A4A4A),
                        fontSize = 16.sp
                    )
                }
                if (enablePh) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = phMin,
                            onValueChange = { phMin = it; validatePh() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            placeholder = { Text("Mínimo") },
                            isError = phError != null
                        )
                        OutlinedTextField(
                            value = phMax,
                            onValueChange = { phMax = it; validatePh() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            placeholder = { Text("Máximo") },
                            isError = phError != null
                        )
                    }
                    if (phError != null) {
                        Text(
                            text = phError!!,
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Define Temperature card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SquareSwitch(
                        checked = enableTemp,
                        onCheckedChange = { enableTemp = it }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Definir Temperatura",
                        color = Color(0xFF4A4A4A),
                        fontSize = 16.sp
                    )
                }
                if (enableTemp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = tempMin,
                            onValueChange = { tempMin = it; validateTemp() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            placeholder = { Text("Mínimo") },
                            isError = tempError != null
                        )
                        OutlinedTextField(
                            value = tempMax,
                            onValueChange = { tempMax = it; validateTemp() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            placeholder = { Text("Máximo") },
                            isError = tempError != null
                        )
                    }
                    if (tempError != null) {
                        Text(
                            text = tempError!!,
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val isFormValid = (if (enablePh) phError == null else true) &&
                (if (enableTemp) tempError == null else true)

            Button(
                onClick = {
                    if (isFormValid) {
                        val tank = Tank(
                            name = if (tankName.isBlank()) "Tanque 1" else tankName,
                            phMin = if (enablePh) phMin.toDoubleOrNull() else null,
                            phMax = if (enablePh) phMax.toDoubleOrNull() else null,
                            tempMin = if (enableTemp) tempMin.toDoubleOrNull() else null,
                            tempMax = if (enableTemp) tempMax.toDoubleOrNull() else null
                        )
                        viewModel.saveTank(tank)
                        onComplete()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                enabled = isFormValid
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "COMPLETAR", color = Color.White)
            }
        }
    }
}

@Composable
fun SquareSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(RectangleShape)
            .background(
                if (checked) Color(0xFF007AFF) else Color.White
            )
            .border(
                width = 1.dp,
                color = if (checked) Color(0xFF007AFF) else Color(0xFFD0D0D0),
                shape = RectangleShape
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}


