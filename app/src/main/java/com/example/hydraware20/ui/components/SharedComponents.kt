package com.example.hydraware20.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.hydraware20.ui.theme.MountainBlue
import com.example.hydraware20.ui.theme.PlanetBlue

@Composable
fun BackgroundElements() {
    // This would contain background elements like planets, mountains, etc.
    // For now, we'll use a simple gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        PlanetBlue.copy(alpha = 0.3f),
                        Color.Transparent,
                        MountainBlue.copy(alpha = 0.2f)
                    ),
                    radius = 800f
                )
            )
    )
}
