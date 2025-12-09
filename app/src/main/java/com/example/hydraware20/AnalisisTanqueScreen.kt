package com.example.hydraware20

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.hydraware20.model.Tanque
import com.example.hydraware20.viewModel.DataSimulatorViewModel
import com.example.hydraware20.viewModel.EstadoValor

@Composable
fun AnalisisTanqueScreen(
    tanque: Tanque,
    onBackClick: () -> Unit
) {
    val viewModel: DataSimulatorViewModel = viewModel(
        factory = DataSimulatorViewModelFactory(tanque)
    )
    
    val phData by viewModel.phDataPoints.collectAsState()
    val tempData by viewModel.temperaturaDataPoints.collectAsState()
    val currentPh by viewModel.currentPh.collectAsState()
    val currentTemp by viewModel.currentTemperatura.collectAsState()
    val phEstado by viewModel.phEstado.collectAsState()
    val tempEstado by viewModel.temperaturaEstado.collectAsState()
    
    // Animación para la línea del gráfico
    val infiniteTransition = rememberInfiniteTransition(label = "graph")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )
    
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
                        tint = Color.Black
                    )
                }
                
                Text(
                    text = "HYDRAWARE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E40AF),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            // Analysis banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E40AF)
                )
            ) {
                Text(
                    text = "Análisis de ${tanque.nombre}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            // Data section
            Text(
                text = "Datos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            // pH Chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Niveles de Ph",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Current value with color indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "pH: ${String.format("%.1f", currentPh)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = viewModel.getEstadoColor(phEstado)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Chart - usar valores del tanque registrado
                    val phMin = tanque.phMin ?: 0f
                    val phMax = tanque.phMax ?: 14f
                    LineChart(
                        dataPoints = phData,
                        maxValue = maxOf(phMax, phData.maxOfOrNull { it.value } ?: phMax) + 1f,
                        minValue = minOf(phMin, phData.minOfOrNull { it.value } ?: phMin) - 1f,
                        animatedProgress = animatedProgress,
                        lineColor = viewModel.getEstadoColor(phEstado),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
            
            // Temperature Chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Niveles de Temperatura",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Current value with color indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Temp: ${String.format("%.1f", currentTemp)}°C",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = viewModel.getEstadoColor(tempEstado)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Chart - usar valores del tanque registrado
                    val tempMin = tanque.temperaturaMin ?: 0f
                    val tempMax = tanque.temperaturaMax ?: 50f
                    LineChart(
                        dataPoints = tempData,
                        maxValue = maxOf(tempMax, tempData.maxOfOrNull { it.value } ?: tempMax) + 5f,
                        minValue = minOf(tempMin, tempData.minOfOrNull { it.value } ?: tempMin) - 5f,
                        animatedProgress = animatedProgress,
                        lineColor = viewModel.getEstadoColor(tempEstado),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun LineChart(
    dataPoints: List<com.example.hydraware20.viewModel.DataPoint>,
    maxValue: Float,
    minValue: Float,
    animatedProgress: Float,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    if (dataPoints.isEmpty()) return
    
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 20.dp.toPx()
        
        val chartWidth = width - padding * 2
        val chartHeight = height - padding * 2
        
        // Draw grid lines
        val gridLines = 5
        val gridColor = Color(0xFFE0E0E0)
        
        for (i in 0..gridLines) {
            val y = padding + (chartHeight / gridLines) * i
            drawLine(
                color = gridColor,
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1.dp.toPx()
            )
            
            // Y-axis labels will be drawn separately using Text composables
        }
        
        // Draw data line
        if (dataPoints.isNotEmpty() && chartWidth > 0 && chartHeight > 0) {
            // Proteger contra división por cero - calcular una sola vez
            val valueRange = (maxValue - minValue).coerceAtLeast(0.1f)
            
            val path = Path()
            val pointSpacing = if (dataPoints.size > 1) {
                chartWidth / (dataPoints.size - 1)
            } else {
                0f
            }
            
            dataPoints.forEachIndexed { index, point ->
                val x = padding + pointSpacing * index
                val normalizedValue = ((point.value - minValue) / valueRange).coerceIn(0f, 1f)
                val y = padding + chartHeight * (1f - normalizedValue)
                
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            
            // Draw animated line - simplified approach
            val visiblePoints = (dataPoints.size * animatedProgress).toInt().coerceAtMost(dataPoints.size)
            
            if (visiblePoints > 1) {
                val animatedPath = Path()
                
                for (i in 0 until visiblePoints) {
                    val x = padding + pointSpacing * i
                    val normalizedValue = ((dataPoints[i].value - minValue) / valueRange).coerceIn(0f, 1f)
                    val y = padding + chartHeight * (1f - normalizedValue)
                    
                    if (i == 0) {
                        animatedPath.moveTo(x, y)
                    } else {
                        animatedPath.lineTo(x, y)
                    }
                }
                
                drawPath(
                    path = animatedPath,
                    color = lineColor,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
            
            // Draw data points
            dataPoints.forEachIndexed { index, point ->
                val x = padding + pointSpacing * index
                val normalizedValue = ((point.value - minValue) / valueRange).coerceIn(0f, 1f)
                val y = padding + chartHeight * (1f - normalizedValue)
                
                if (index < visiblePoints) {
                    drawCircle(
                        color = Color(0xFF007AFF),
                        radius = 4.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}

// Factory for DataSimulatorViewModel
class DataSimulatorViewModelFactory(
    private val tanque: Tanque
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataSimulatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DataSimulatorViewModel(tanque) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

