package com.example.to_dolist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.to_dolist.ui.theme.ThemeAccent
import com.example.to_dolist.ui.theme.ThemeModeOption
import com.example.to_dolist.ui.theme.WallpaperOption

@Composable
fun SettingsDialog(
    currentThemeAccent: ThemeAccent,
    currentThemeMode: ThemeModeOption,
    currentWallpaper: WallpaperOption,
    onThemeAccentSelected: (ThemeAccent) -> Unit,
    onThemeModeSelected: (ThemeModeOption) -> Unit,
    onWallpaperSelected: (WallpaperOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Appearance & Theme Settings") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Theme Mode Section
                Text(
                    text = "Theme Mode",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ThemeModeOption.entries.forEach { mode ->
                        val label = when (mode) {
                            ThemeModeOption.LIGHT -> "Light"
                            ThemeModeOption.DARK -> "Dark"
                            ThemeModeOption.SYSTEM -> "System"
                            ThemeModeOption.TRANSLUCENT -> "Glass"
                        }
                        FilterChip(
                            selected = currentThemeMode == mode,
                            onClick = { onThemeModeSelected(mode) },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Accent Color Section
                Text(
                    text = "Accent Color",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ThemeAccent.entries.forEach { accent ->
                        val color = when (accent) {
                            ThemeAccent.DEFAULT -> Color(0xFF6750A4)
                            ThemeAccent.BLUE -> Color(0xFF0061A4)
                            ThemeAccent.PURPLE -> Color(0xFFD0BCFF)
                            ThemeAccent.GREEN -> Color(0xFF006D3B)
                            ThemeAccent.ORANGE -> Color(0xFF8F4C36)
                            ThemeAccent.TEAL -> Color(0xFF006874)
                            ThemeAccent.PINK -> Color(0xFF984061)
                        }
                        val isSelected = currentThemeAccent == accent
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { onThemeAccentSelected(accent) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }
                    }
                }

                // Wallpaper Section (Visible only when Translucent mode is active)
                if (currentThemeMode == ThemeModeOption.TRANSLUCENT) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "Glassmorphism Wallpaper",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        WallpaperOption.entries.forEach { wallpaper ->
                            val isSelected = currentWallpaper == wallpaper
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onWallpaperSelected(wallpaper) },
                                shape = MaterialTheme.shapes.medium,
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = wallpaper.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { onWallpaperSelected(wallpaper) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}
